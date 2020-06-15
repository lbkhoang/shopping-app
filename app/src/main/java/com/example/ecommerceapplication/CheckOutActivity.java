package com.example.ecommerceapplication;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
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

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ecommerceapplication.Model.*;
import com.example.ecommerceapplication.ViewHolder.OrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.*;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import com.squareup.picasso.Picasso;
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

    private DatabaseReference OrderRef;

    private TextView edtAmount, name;
    private EditText note, address;
    private String amount = "";
    private String count;
    private Map<String,Object> orderData;
    private Users user;
    private RecyclerView recyclerView;

    @Override
    protected void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_out_detail);

        Paper.init(this);
        user = Paper.book().read("userDetail");

        amount = getIntent().getStringExtra("amount");

        OrderRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(user.getPhone());

        //start paypal service
        Intent intent = new Intent(this,PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
        startService(intent);

        Button btnPayNow = findViewById(R.id.btnPayNow);
        Button btnPayLater = findViewById(R.id.btnPayLater);

        edtAmount = findViewById(R.id.total);
        address = findViewById(R.id.address);
        note = findViewById(R.id.note);
        name = findViewById(R.id.user_name);

        recyclerView = findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        edtAmount.setText(amount);
        name.setText(user.getName());

        loadOrderProductData();

        btnPayNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String a = address.getText().toString();
                if (validateInfo(a)) {
                    Toast.makeText(CheckOutActivity.this, "Invalid Address", Toast.LENGTH_SHORT).show();
                } else {
                    processPayment();
                    delCart();
                }
            }
        });
        btnPayLater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String a = address.getText().toString();
                if (validateInfo(a)) {
                    Toast.makeText(CheckOutActivity.this, "Invalid Address", Toast.LENGTH_SHORT).show();
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

    private boolean validateInfo(String a) {
        return "".equals(a);
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
                                .putExtra("apiRespond", count)
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
        orderData.put("address", address.getText() == null ? "" : address.getText().toString());
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

    private void loadOrderProductData() {
        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(OrderRef, Products.class)
                        .build();

        FirebaseRecyclerAdapter<Products, OrderViewHolder> adapter =
                new FirebaseRecyclerAdapter<Products, OrderViewHolder>(options) {
                    @NonNull
                    @Override
                    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_detail_layout, parent, false);
                        return new OrderViewHolder(view);
                    }

                    @Override
                    protected void onBindViewHolder(@NonNull OrderViewHolder holder, int position, @NonNull final Products model) {
                        int qty = Integer.parseInt(model.getQuantity());
                        float price = Integer.parseInt(model.getPrice());
                        holder.txtProductName.setText(qty+ " x " + model.getPname());
                        Picasso.get().load(model.getImage()).into(holder.imageView);
                        holder.txtProductPrice.setText("$" + qty*price);
                    }
                };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}
