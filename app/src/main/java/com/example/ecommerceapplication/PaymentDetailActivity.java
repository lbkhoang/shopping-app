package com.example.ecommerceapplication;

import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class PaymentDetailActivity extends AppCompatActivity {

    TextView txtId,txtAmount,txtStatus;
    String amount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_detail);

        txtId = findViewById(R.id.txtId);
        txtAmount = findViewById(R.id.txtAmount);
        txtStatus = findViewById(R.id.txtStatus);

        amount = getIntent().getStringExtra("amount");

        txtAmount.setText("$"+amount);

    }


}
