package com.example.ecommerceapplication;


import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.ecommerceapplication.Model.Users;
import com.squareup.picasso.Picasso;
import io.paperdb.Paper;

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

        Paper.init(this);
        Users user = Paper.book().read("userDetail");

        if (user.getImage() != null) {
            Picasso.get().load(user.getImage()).into(profilePic);
        }

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

}
