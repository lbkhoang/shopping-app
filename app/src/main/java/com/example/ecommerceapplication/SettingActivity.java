package com.example.ecommerceapplication;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.ecommerceapplication.Model.Users;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import io.paperdb.Paper;

import java.util.HashMap;

public class SettingActivity extends AppCompatActivity {

    private EditText userName, userAddress;
    private Button saveBtn;
    private Users user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        Paper.init(this);
        user = Paper.book().read("userDetail");

        userName = findViewById(R.id.user_name);
        userAddress = findViewById(R.id.user_address);

        saveBtn = findViewById(R.id.save_btn);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("user").child(user.getPhone());
                HashMap<String,Object> userData = new HashMap<>();
                userData.put("address", userAddress.getText().toString());
                userData.put("name", userAddress.getText().toString());
                dbRef.updateChildren(userData);

            }
        });

    }
}
