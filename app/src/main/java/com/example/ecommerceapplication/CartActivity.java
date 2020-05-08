package com.example.ecommerceapplication;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.firebase.database.*;

import static android.util.Log.d;

public class CartActivity extends AppCompatActivity {

    private TextView textView;
    private Button happyButton, sadButton;

    DatabaseReference root = FirebaseDatabase.getInstance().getReference();
    DatabaseReference databaseReference = root.child("condition");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        textView = findViewById(R.id.test);
        happyButton = findViewById(R.id.happy_btn);
        sadButton = findViewById(R.id.sad_btn);
    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String text = dataSnapshot.getValue(String.class);
                textView.setText(text);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        happyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.setValue("Happy");
            }
        });

        sadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.setValue("Sad");
            }
        });
    }
}
