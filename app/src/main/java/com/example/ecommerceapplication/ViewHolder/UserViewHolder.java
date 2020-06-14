package com.example.ecommerceapplication.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ecommerceapplication.R;

public class UserViewHolder extends RecyclerView.ViewHolder {
    public ImageView imageView;
    public TextView txtUserName, txtUserStatus, txtUserDescription;

    public UserViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.user_image);
        txtUserName = itemView.findViewById(R.id.user_name);
        txtUserStatus = itemView.findViewById(R.id.user_status);
        txtUserDescription = itemView.findViewById(R.id.user_description);
    }
}
