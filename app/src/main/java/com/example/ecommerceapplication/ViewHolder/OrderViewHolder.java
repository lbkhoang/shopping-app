package com.example.ecommerceapplication.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ecommerceapplication.Interface.ItemClick;
import com.example.ecommerceapplication.R;

public class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    public TextView txtProductName, txtProductQuantity, txtProductPrice;
    public ImageView imageView;
    public ItemClick listener;


    public OrderViewHolder(View itemView)
    {
        super(itemView);


        imageView = (ImageView) itemView.findViewById(R.id.product_image);
        txtProductName = (TextView) itemView.findViewById(R.id.product_name);
        txtProductQuantity = (TextView) itemView.findViewById(R.id.product_quantity);
        txtProductPrice = (TextView) itemView.findViewById(R.id.product_price);
    }

//    public void setItemClick(ItemClick listener)
//    {
//        this.listener = listener;
//    }

    @Override
    public void onClick(View view)
    {
        listener.onClick(view, getAdapterPosition(), false);
    }
}
