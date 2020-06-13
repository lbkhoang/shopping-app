package com.example.ecommerceapplication;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class AdminCategoryActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);

        //drinks
        ImageView coffee = (ImageView) findViewById(R.id.coffee);
        ImageView iceBlend = (ImageView) findViewById(R.id.ice_blend);
        ImageView softDrink = (ImageView) findViewById(R.id.soft_drink);
        ImageView water = (ImageView) findViewById(R.id.water);

        //deserts
        ImageView cake = (ImageView) findViewById(R.id.cake);
        ImageView cupcake = (ImageView) findViewById(R.id.cupcake);
        ImageView cookie = (ImageView) findViewById(R.id.cookie);
        ImageView bread = (ImageView) findViewById(R.id.bread);

        coffee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "coffee");
                startActivity(intent);
            }
        });


        iceBlend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "iceBlend");
                startActivity(intent);
            }
        });


        softDrink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "softDrink");
                startActivity(intent);
            }
        });


        water.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "water");
                startActivity(intent);
            }
        });


        cake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "cake");
                startActivity(intent);
            }
        });


        cupcake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "cupcake");
                startActivity(intent);
            }
        });


        cookie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "cookie");
                startActivity(intent);
            }
        });


        bread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "bread");
                startActivity(intent);
            }
        });
    }
}
