package com.example.ecommerceapplication;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.ecommerceapplication.Model.Products;
import com.example.ecommerceapplication.Model.Users;
import com.google.firebase.database.*;
import com.squareup.picasso.Picasso;
import io.paperdb.Paper;

public class AddToCartActivity extends AppCompatActivity {

    private DatabaseReference ProductsRef;
    private TextView txtProductName, txtProductDescription, txtProductPrice, txtQuantity;
    private ImageView imageView;
    private Button addButton, removeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_cart);

        Paper.init(this);
        Users user = Paper.book().read("userDetail");

        String pId = getIntent().getStringExtra("pId");

        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products").child(pId);

        imageView = findViewById(R.id.product_image);
        txtProductName = findViewById(R.id.product_name);
        txtProductDescription = findViewById(R.id.product_description);
        txtProductPrice = findViewById(R.id.product_price);
        txtQuantity = findViewById(R.id.product_quantity);
        addButton = findViewById(R.id.add_btn);
        removeButton = findViewById(R.id.remove_btn);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int value = Integer.parseInt(txtQuantity.getText().toString());
                value = value + 1;
                txtQuantity.setText(Integer.toString(value));
            }
        });

        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int value = Integer.parseInt(txtQuantity.getText().toString());
                if (value != 0) {
                    value = value - 1;
                }
                txtQuantity.setText(Integer.toString(value));
            }
        });


        loadProductData();
    }


    private void loadProductData() {

        ProductsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Products productsData = dataSnapshot.getValue(Products.class);
                txtProductName.setText(productsData.getPname());
                txtProductDescription.setText(productsData.getDescription());
                txtProductPrice.setText(productsData.getPrice());
                Picasso.get().load(productsData.getImage()).into(imageView);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AddToCartActivity.this, "Error Loading Data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
