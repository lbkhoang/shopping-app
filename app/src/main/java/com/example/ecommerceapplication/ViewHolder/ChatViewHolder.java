package com.example.ecommerceapplication.ViewHolder;

import android.view.View;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ecommerceapplication.R;

public class ChatViewHolder extends RecyclerView.ViewHolder{
    public TextView txtUserName, txtUserChat;
    //public ImageView imageView;


    public ChatViewHolder(View itemView)
    {
        super(itemView);


        //imageView = (ImageView) itemView.findViewById(R.id.product_image);
        //txtProductPrice = (TextView) itemView.findViewById(R.id.product_price);
        txtUserName = itemView.findViewById(R.id.user_name);
        txtUserChat = itemView.findViewById(R.id.user_chat);
    }
}
