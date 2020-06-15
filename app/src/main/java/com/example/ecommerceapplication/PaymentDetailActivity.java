package com.example.ecommerceapplication;

import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import org.json.JSONException;
import org.json.JSONObject;

public class PaymentDetailActivity extends AppCompatActivity {

    private TextView txtId,txtAmount,txtStatus;
    private String amount, apiRespond;
    private Users user;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_detail);

        Paper.init(this);
        user = Paper.book().read("userDetail");
        if (user.getRole() == null) {
            user.setRole("");
        }


        txtId = findViewById(R.id.txtId);
        txtAmount = findViewById(R.id.txtAmount);
        txtStatus = findViewById(R.id.txtStatus);

        apiRespond = getIntent().getStringExtra("apiRespond");
        amount = getIntent().getStringExtra("amount");

        //txtStatus.setText("Thanks for your purchase, you can check your order in order menu");

        setBottomNavBar();

    }

    private void setBottomNavBar() {

        FloatingActionButton floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PaymentDetailActivity.this, EditCartActivity.class);
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
                intent = new Intent(PaymentDetailActivity.this, UserChatActivity.class);
                startActivity(intent);
                return true;
            case R.id.cart_app_bar:
                intent = new Intent(PaymentDetailActivity.this, WishList.class);
                startActivity(intent);
                return true;
            case R.id.order_app_bar:
                intent = new Intent(PaymentDetailActivity.this, OrderListActivity.class);
                startActivity(intent);
                return true;
            case R.id.search_app_bar:
                intent = new Intent(PaymentDetailActivity.this, Search.class);
                startActivity(intent);
                return true;
            case R.id.profile_app_bar:
                intent = new Intent(PaymentDetailActivity.this, ProfileActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
