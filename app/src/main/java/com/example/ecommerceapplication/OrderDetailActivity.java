package com.example.ecommerceapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ecommerceapplication.Model.Products;
import com.example.ecommerceapplication.ViewHolder.OrderListViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OrderDetailActivity extends AppCompatActivity {

    private DatabaseReference OrderRef;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        String orderId = getIntent().getStringExtra("orderId");

        OrderRef = FirebaseDatabase.getInstance().getReference().child("ConfirmedOrder").child(orderId).child("Product");

        recyclerView = findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        loadOrderProductData();

        loadOrderData();
    }

    private void loadOrderData() {
        //TODO
    }

    private void loadOrderProductData() {
        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(OrderRef, Products.class)
                        .build();

        FirebaseRecyclerAdapter<Products, OrderListViewHolder> adapter = new FirebaseRecyclerAdapter<Products, OrderListViewHolder>(options) {
            @NonNull
            @Override
            public OrderListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_layout, parent, false);
                return new OrderListViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull OrderListViewHolder holder, int position, @NonNull final Products model) {
                holder.txtOrderName.setText(model.getPname());
                holder.txtOrderCost.setText(model.getQuantity());
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }


}
