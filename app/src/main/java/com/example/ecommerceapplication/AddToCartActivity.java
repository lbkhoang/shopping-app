package com.example.ecommerceapplication;

import android.content.Intent;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.ecommerceapplication.Model.Products;
import com.example.ecommerceapplication.Model.Users;
import com.google.firebase.database.*;
import com.squareup.picasso.Picasso;
import io.paperdb.Paper;

public class AddToCartActivity extends AppCompatActivity {

    private DatabaseReference ProductsRef, FavRef;
    private TextView txtProductName, txtProductDescription, txtProductPrice, txtQuantity, txtTotal;
    private ImageView imageView;
    private ImageButton favbutton;
    private Products productsData;
    private Users user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_cart);

        Paper.init(this);
        user = Paper.book().read("userDetail");

        String pId = getIntent().getStringExtra("pId");
        String amount = getIntent().getStringExtra("amount");

        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products").child(pId);
        FavRef = FirebaseDatabase.getInstance().getReference().child("user").child(user.getPhone()).child("favorite");

        imageView = findViewById(R.id.product_image);
        txtProductName = findViewById(R.id.product_name);
        txtProductDescription = findViewById(R.id.product_description);
        txtProductPrice = findViewById(R.id.product_price);
        txtQuantity = findViewById(R.id.product_quantity);
        txtTotal = findViewById(R.id.txtTotal);
        ImageButton increaseButton = findViewById(R.id.add_btn);
        ImageButton decreaseButton = findViewById(R.id.remove_btn);
        Button addButton = findViewById(R.id.add_to_cart_btn);
        favbutton = findViewById(R.id.fav_btn);


        increaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int prdPrice = Integer.parseInt(productsData.getPrice());
                int value = Integer.parseInt(txtQuantity.getText().toString());
                value = value + 1;
                txtQuantity.setText(Integer.toString(value));
                txtTotal.setText("Total: $" + (value * prdPrice));
            }
        });

        decreaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int prdPrice = Integer.parseInt(productsData.getPrice());
                int value = Integer.parseInt(txtQuantity.getText().toString());
                if (value != 0) {
                    value = value - 1;
                }
                txtQuantity.setText(Integer.toString(value));
                txtTotal.setText("Total: $" + (value * prdPrice));
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCart();
                Intent intent = new Intent(AddToCartActivity.this, EditCartActivity.class);
                startActivity(intent);
                finish();
            }
        });

        favbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToFav();
            }
        });

        favbutton.setImageResource(R.drawable.ic_favorite_24dp);

        loadProductData();

        loadFavData();

        txtQuantity.setText(amount == null ? "0" : amount);
    }

    private void addToFav() {

        FavRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(productsData.getPid()).exists()) {
                    dataSnapshot.child(productsData.getPid()).getRef().removeValue();
                    favbutton.setImageResource(R.drawable.ic_favorite_border_24dp);
                    Toast.makeText(AddToCartActivity.this, "removed", Toast.LENGTH_SHORT).show();
                } else {
                    dataSnapshot.child(productsData.getPid()).getRef().setValue(productsData);
                    Toast.makeText(AddToCartActivity.this, "added", Toast.LENGTH_SHORT).show();
                    favbutton.setImageResource(R.drawable.ic_favorite_24dp);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void loadFavData(){
        FavRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(productsData.getPid()).exists()) {
                    favbutton.setImageResource(R.drawable.ic_favorite_24dp);
                } else {
                    favbutton.setImageResource(R.drawable.ic_favorite_border_24dp);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void loadProductData() {

        ProductsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productsData = dataSnapshot.getValue(Products.class);
                txtProductName.setText(productsData.getPname());
                txtProductDescription.setText(productsData.getDescription());
                txtProductPrice.setText("Price: $" + productsData.getPrice());
                Picasso.get().load(productsData.getImage()).into(imageView);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AddToCartActivity.this, "Error Loading Data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addToCart() {

        productsData.setQuantity(txtQuantity.getText().toString());
        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Orders");
        ProductsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (productsData.getQuantity().equals("0")) {
                    dataSnapshot.child(user.getPhone()).child(productsData.getPid()).getRef().removeValue();
                } else {
                    dataSnapshot.child(user.getPhone()).child(productsData.getPid()).getRef().setValue(productsData);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
