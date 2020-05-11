package com.example.ecommerceapplication;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.ecommerceapplication.Model.Users;
import com.google.firebase.database.*;
import io.paperdb.Paper;

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

        Paper.init(this);
        Users user = Paper.book().read("userDetail");

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
                //databaseReference.setValue("Happy");

                Intent intent = new Intent(CartActivity.this, CheckOutActivity.class);
                startActivity(intent);

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
