package com.example.ecommerceapplication;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ecommerceapplication.Model.ConfirmedOrder;
import com.example.ecommerceapplication.Model.OrderList;
import com.example.ecommerceapplication.ViewHolder.OrderListViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OrderListActivity extends AppCompatActivity {

    private DatabaseReference OrderRef;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

        OrderRef = FirebaseDatabase.getInstance().getReference().child("ConfirmedOrder");

        recyclerView = findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        loadOrderData();


    }

    private void loadOrderData() {
        FirebaseRecyclerOptions<ConfirmedOrder> options =
                new FirebaseRecyclerOptions.Builder<ConfirmedOrder>()
                        .setQuery(OrderRef, ConfirmedOrder.class)
                        .build();

        FirebaseRecyclerAdapter<ConfirmedOrder, OrderListViewHolder> adapter =
                new FirebaseRecyclerAdapter<ConfirmedOrder, OrderListViewHolder>(options) {
            @NonNull
            @Override
            public OrderListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_layout, parent, false);
                return new OrderListViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull OrderListViewHolder holder, int position, @NonNull final ConfirmedOrder model) {
                holder.txtOrderName.setText("Order Number " + model.getOrderId());
                holder.txtOrderCost.setText("$" + model.getCost() + " - " + model.getPayMethod());
                holder.txtOrderDate.setText("Date: " + model.getDate() + " - " + model.getTime());

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(OrderListActivity.this, OrderDetailActivity.class);
                        intent.putExtra("orderId", model.getDate() + " - " + model.getTime());
                        startActivity(intent);
                    }
                });
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}
