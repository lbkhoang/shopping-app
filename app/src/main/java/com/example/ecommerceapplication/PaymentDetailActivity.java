package com.example.ecommerceapplication;

import android.util.Log;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import org.json.JSONException;
import org.json.JSONObject;

public class PaymentDetailActivity extends AppCompatActivity {

    TextView txtId,txtAmount,txtStatus;
    String amount;
    String apiRespond;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_detail);

        txtId = findViewById(R.id.txtId);
        txtAmount = findViewById(R.id.txtAmount);
        txtStatus = findViewById(R.id.txtStatus);

        apiRespond = getIntent().getStringExtra("apiRespond");

        Log.d("pay", "onCreate: " + apiRespond);
        amount = getIntent().getStringExtra("amount");

        txtAmount.setText("$"+amount);
        txtStatus.setText("Thanks for purchase, your order id is:");
        txtId.setText(apiRespond);

    }


}
