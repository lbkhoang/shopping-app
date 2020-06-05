package com.example.ecommerceapplication.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ecommerceapplication.R;

public class OrderListViewHolder extends RecyclerView.ViewHolder{
    public TextView txtOrderName, txtOrderCost, txtOrderDate;


    public OrderListViewHolder(View itemView)
    {
        super(itemView);

        txtOrderName = itemView.findViewById(R.id.order_name);
        txtOrderCost = itemView.findViewById(R.id.order_cost);
        txtOrderDate = itemView.findViewById(R.id.order_date);
    }
}
