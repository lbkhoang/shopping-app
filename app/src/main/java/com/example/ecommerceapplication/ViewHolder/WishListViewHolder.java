package com.example.ecommerceapplication.ViewHolder;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ecommerceapplication.R;

public class WishListViewHolder extends RecyclerView.ViewHolder {

    public TextView txtProductName, txtProductQuantity, txtProductPrice;
    public ImageView imageView;
    public ImageButton imageButton;


    public WishListViewHolder(View itemView)
    {
        super(itemView);

        imageView = itemView.findViewById(R.id.product_image);
        txtProductName = itemView.findViewById(R.id.product_name);
        txtProductQuantity = itemView.findViewById(R.id.product_quantity);
        txtProductPrice = itemView.findViewById(R.id.product_price);
        imageButton = itemView.findViewById(R.id.product_remove);
    }
}
