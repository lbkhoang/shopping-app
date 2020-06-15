package com.example.ecommerceapplication;

import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ecommerceapplication.Model.Users;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.*;
import io.paperdb.Paper;

public class AdminCategoryActivity extends AppCompatActivity
{
    private Users user;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);

        Paper.init(this);
        user = Paper.book().read("userDetail");
        if (user.getRole() == null) {
            user.setRole("");
        }

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

        setBottomNavBar();
    }

    private void setBottomNavBar() {

        FloatingActionButton floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, EditCartActivity.class);
                startActivity(intent);
            }
        });


        Toolbar toolbar = findViewById(R.id.bottom_app_bar);
        setSupportActionBar(toolbar);

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference()
                .child("Orders").child(user.getPhone());

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                TextView textView = findViewById(R.id.notification_badge);
                textView.setText(String.valueOf(dataSnapshot.getChildrenCount()));
                Log.d("main", "onDataChange: " + dataSnapshot.getChildrenCount());
                if (!dataSnapshot.exists()){
                    textView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()){
            case R.id.home_app_bar:
                intent = new Intent(AdminCategoryActivity.this, UserChatActivity.class);
                startActivity(intent);
                return true;
            case R.id.cart_app_bar:
                intent = new Intent(AdminCategoryActivity.this, WishList.class);
                startActivity(intent);
                return true;
            case R.id.order_app_bar:
                intent = new Intent(AdminCategoryActivity.this, OrderListActivity.class);
                startActivity(intent);
                return true;
            case R.id.search_app_bar:
                intent = new Intent(AdminCategoryActivity.this, Search.class);
                startActivity(intent);
                return true;
            case R.id.profile_app_bar:
                intent = new Intent(AdminCategoryActivity.this, ProfileActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
