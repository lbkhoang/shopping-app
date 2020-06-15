package com.example.ecommerceapplication;


import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import com.example.ecommerceapplication.Model.Users;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.*;
import com.squareup.picasso.Picasso;
import io.paperdb.Paper;

public class ProfileActivity extends AppCompatActivity {

    private ImageView profilePic;
    private TextView txtAddress, txtPassword, txtLogOut, txtAdmin;
    private Users user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profilePic = findViewById(R.id.profile_img);
        txtAddress = findViewById(R.id.address);
        txtPassword = findViewById(R.id.password);
        txtLogOut = findViewById(R.id.logout);
        txtAdmin = findViewById(R.id.admin);

        View view = findViewById(R.id.admin_linear);

        Paper.init(this);
        user = Paper.book().read("userDetail");
        if (user.getRole() == null) {
            view.setVisibility(View.GONE);
        }
        if (user.getImage() != null) {
            Picasso.get().load(user.getImage()).into(profilePic);
        }

        setBottomNavBar();

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

        txtLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmDialog("Log Out", "Are you sure you want Log Out?");
            }
        });

        txtAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });
    }

    private void showConfirmDialog(String title, String message) {
        new AlertDialog.Builder(ProfileActivity.this)
                .setTitle(title)
                .setMessage(message)

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Continue with click operation
                        LogOut();

                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void LogOut() {
        Paper.book().destroy();

        Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void setBottomNavBar() {

        FloatingActionButton floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, EditCartActivity.class);
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
                intent = new Intent(ProfileActivity.this, UserChatActivity.class);
                startActivity(intent);
                return true;
            case R.id.cart_app_bar:
                intent = new Intent(ProfileActivity.this, WishList.class);
                startActivity(intent);
                return true;
            case R.id.order_app_bar:
                intent = new Intent(ProfileActivity.this, OrderListActivity.class);
                startActivity(intent);
                return true;
            case R.id.search_app_bar:
                intent = new Intent(ProfileActivity.this, Search.class);
                startActivity(intent);
                return true;
            case R.id.profile_app_bar:
                intent = new Intent(ProfileActivity.this, ProfileActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
