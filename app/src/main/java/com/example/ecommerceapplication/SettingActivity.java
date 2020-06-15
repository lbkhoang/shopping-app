package com.example.ecommerceapplication;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.ecommerceapplication.Model.UserAddress;
import com.example.ecommerceapplication.Model.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.*;
import io.paperdb.Paper;

import java.util.HashMap;

public class SettingActivity extends AppCompatActivity {

    private EditText txtUserCity, txtUserAddress, txtUserState;
    private Button saveBtn;
    private Users user;
    private DatabaseReference dbRef;
    private UserAddress userAddress = new UserAddress();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        Paper.init(this);
        user = Paper.book().read("userDetail");
        dbRef = FirebaseDatabase.getInstance().getReference()
                        .child("user").child(user.getPhone()).child("address");

        txtUserState = findViewById(R.id.user_state);
        txtUserAddress = findViewById(R.id.user_address);
        txtUserCity = findViewById(R.id.user_city);

        saveBtn = findViewById(R.id.save_btn);

        setData();

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userAddress.setAddress(txtUserAddress.getText().toString());
                userAddress.setCity(txtUserCity.getText().toString());
                userAddress.setState(txtUserState.getText().toString());
                dbRef.setValue(userAddress).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(SettingActivity.this, "Address Updated", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SettingActivity.this, ProfileActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });


            }
        });
    }

    private void setData() {
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    userAddress = dataSnapshot.getValue(UserAddress.class);
                } catch (Exception e){

                }
                if (userAddress != null) {
                    if (userAddress.getAddress() != null) {
                        txtUserAddress.setText(userAddress.getAddress());
                    }

                    if (userAddress.getCity() != null) {
                        txtUserCity.setText(userAddress.getCity());
                    }

                    if (userAddress.getState() != null) {
                        txtUserState.setText(userAddress.getState());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
