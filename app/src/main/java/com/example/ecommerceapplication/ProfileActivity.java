package com.example.ecommerceapplication;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class ProfileActivity extends AppCompatActivity {

    private ImageView profilePic;
    private TextView txtAddress, txtPassword, txtLogOut, txtAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profilePic = findViewById(R.id.profile_img);
        txtAddress = findViewById(R.id.address);
        txtPassword = findViewById(R.id.password);
        txtLogOut = findViewById(R.id.logout);
        txtAdmin = findViewById(R.id.admin);

        setOnClick();


    }

    private void setOnClick() {
        txtAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, AdminCategoryActivity.class);
                startActivity(intent);
            }
        });
    }
}
