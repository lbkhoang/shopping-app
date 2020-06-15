package com.example.ecommerceapplication;

import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.ecommerceapplication.Model.*;
import com.google.firebase.database.*;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import io.paperdb.Paper;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CheckOutActivity extends AppCompatActivity {

    private static final int PAYPAL_REQUEST_CODE = 7777;

    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(Config.PAYPAL_CLIENT_ID);

    private TextView edtAmount, address;
    private EditText note;
    private String amount = "";
    private String count;
    private Map<String,Object> orderData;
    private Users user;

    @Override
    protected void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);

        Paper.init(this);
        user = Paper.book().read("userDetail");

        amount = getIntent().getStringExtra("amount");

        //start paypal service
        Intent intent = new Intent(this,PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
        startService(intent);

        Button btnPayNow = findViewById(R.id.btnPayNow);
        Button btnPayLater = findViewById(R.id.btnPayLater);
        edtAmount = findViewById(R.id.edtAmount);

        address = findViewById(R.id.address);

        note = findViewById(R.id.note);

        //addChildObserver();

        edtAmount.setText(amount);

        btnPayNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processPayment();
            }
        });
        btnPayLater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String a = address.getText().toString();
                if ("".equals(a)) {
                    Toast.makeText(CheckOutActivity.this, "Invalid", Toast.LENGTH_SHORT).show();
                } else {
                    saveOrder("COD");
                    Intent intent = new Intent(CheckOutActivity.this, PaymentDetailActivity.class);
                    intent.putExtra("apiRespond", count).putExtra("amount",amount);
                    startActivity(intent);
                    finish();
                    delCart();
                }
            }
        });
    }

    private void delCart() {
        //TODO del cart
    }

    private void processPayment() {
        amount = edtAmount.getText().toString();
        amount = amount.replace("Total: $", "");

        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(String.valueOf(amount)),"USD",
                "Purchase Goods",PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT,payPalPayment);
        startActivityForResult(intent,PAYPAL_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        String apiRespond = "";
        if (requestCode == PAYPAL_REQUEST_CODE){
            if (resultCode == RESULT_OK){
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirmation != null){
                    try {
                        JSONObject paymentDetails = confirmation.toJSONObject();
                        apiRespond = paymentDetails.getJSONObject("response").get("id").toString();
                        startActivity(new Intent(this,PaymentDetailActivity.class)
                                .putExtra("apiRespond", apiRespond)
                                .putExtra("amount",amount));
                        finish();
                        saveOrder("PayPal");
                    } catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED)
                Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show();
        } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID)
            Toast.makeText(this, "Invalid", Toast.LENGTH_SHORT).show();
    }

    private void saveOrder(final String apiRespond) {
        final DatabaseReference OrderRef = FirebaseDatabase.getInstance().getReference();
        final Orders orders = new Orders();

        amount = edtAmount.getText().toString();
        amount = amount.replace("Total: $", "");

        orderData = new HashMap<>();
        orderData.put("note", note.getText().toString() == null ? "" : note.getText().toString());
        orderData.put("address", address.getText() == null ? "" : note.getText().toString());
        orderData.put("cost", amount);
        getOrderCount();



        OrderRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String key = getDate() + " - " + getTime();
                for (DataSnapshot snapshot : dataSnapshot.child("Orders").child(user.getPhone()).getChildren()) {
                    Products product = snapshot.getValue(Products.class);
                    orders.setPname(product.getPname());
                    orders.setPrice(product.getPrice());
                    orders.setQuantity(product.getQuantity());
                    orders.setImage(product.getImage());
                    OrderRef.child("ConfirmedOrder").child(key).child("Product").child(product.getPid())
                            .getRef().setValue(orders);
                }
                orderData.put("userId", user.getPhone());
                orderData.put("payMethod", apiRespond);
                orderData.put("date", getDate());
                orderData.put("time", getTime());
                OrderRef.child("ConfirmedOrder").child(key).updateChildren(orderData);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getOrderCount() {
        Query query = FirebaseDatabase.getInstance().getReference().child("ConfirmedOrder");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long l = dataSnapshot.getChildrenCount();
                Log.d("TAG1", "onDataChange: " + l);
                l += 1;
                count = String.valueOf(l);
                Log.d("TAG1", "onchildadded: " + count);
                orderData.put("orderId", count);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private String getTime() {
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");

        return currentTime.format(calendar.getTime());
    }

    private String getDate() {
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");

        return currentDate.format(calendar.getTime());
    }
}
