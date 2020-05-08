package com.example.ecommerceapplication;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.*;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    private Button SignUpButton;
    private EditText InputName, InputPhoneNumber, InputPassword, InputRePassword;
    private static final String TAG = "signUpActivity";
    FirebaseDatabase root;
    DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        SignUpButton = (Button) findViewById(R.id.sign_up_btn);
        InputName = (EditText) findViewById(R.id.name_input);
        InputPhoneNumber = (EditText) findViewById(R.id.phone_input);
        InputPassword = (EditText) findViewById(R.id.password_input);
        InputRePassword = (EditText) findViewById(R.id.password_re_input);

        SignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount();
            }
        });
    }

    private void createAccount() {
        String name = InputName.getText().toString();
        String phone = InputPhoneNumber.getText().toString();
        String password = InputPassword.getText().toString();
        String rePassword = InputRePassword.getText().toString();

        if (TextUtils.isEmpty(name)){
            Toast.makeText(this, "Please input your name...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(phone)){
            Toast.makeText(this, "Please input your phone...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please input your password...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(rePassword)){
            Toast.makeText(this, "Please re type your password...", Toast.LENGTH_SHORT).show();
        }
        else if (!password.equals(rePassword)){
            Toast.makeText(this, "Password does not match...", Toast.LENGTH_SHORT).show();
        } else {
            ValidatePhoneNumber(name, phone, password);
        }


    }

    private void ValidatePhoneNumber(final String name, final String phone, final String password) {
        root = FirebaseDatabase.getInstance();
        dbRef = root.getReference("user");
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Boolean bool = dataSnapshot.child(phone).exists();
                Log.d("fffffffffffff", String.valueOf(bool));
                if(!dataSnapshot.child(phone).exists()){
                    Map<String, Object> userData = new HashMap<>();
                    userData.put("phone", phone);
                    userData.put("password", password);
                    userData.put("name", name);
                    dbRef.child(phone).setValue(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(SignUpActivity.this, "Account created", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(SignUpActivity.this, "Network err!!!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(SignUpActivity.this, "Phone number exits", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(SignUpActivity.this, "Error Loading Data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
