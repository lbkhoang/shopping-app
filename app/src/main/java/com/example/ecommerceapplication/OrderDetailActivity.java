package com.example.ecommerceapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ecommerceapplication.Model.ConfirmedOrder;
import com.example.ecommerceapplication.Model.Products;
import com.example.ecommerceapplication.ViewHolder.OrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.*;
import com.squareup.picasso.Picasso;

public class OrderDetailActivity extends AppCompatActivity {

    private DatabaseReference OrderRef;
    private RecyclerView recyclerView;
    private TextView address, cost, date, note, payMethod, name;
    private ConfirmedOrder confirmedOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        address = findViewById(R.id.address);
        cost = findViewById(R.id.total);
        date = findViewById(R.id.time);
        note = findViewById(R.id.note);
        payMethod = findViewById(R.id.pay_method);
        name = findViewById(R.id.order_name);


        String orderId = getIntent().getStringExtra("orderId");

        OrderRef = FirebaseDatabase.getInstance().getReference().child("ConfirmedOrder").child(orderId);

        recyclerView = findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        loadOrderProductData();

        loadOrderData();
    }

    private void loadOrderData() {

        OrderRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                confirmedOrder = dataSnapshot.getValue(ConfirmedOrder.class);
                address.setText(confirmedOrder.getAddress());
                cost.setText("Total: $"+confirmedOrder.getCost());
                date.setText(confirmedOrder.getTime());
                payMethod.setText(confirmedOrder.getPayMethod());
                note.setText(confirmedOrder.getNote());
                name.setText("Order Number "+confirmedOrder.getOrderId());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void loadOrderProductData() {
        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(OrderRef.child("Product"), Products.class)
                        .build();

        FirebaseRecyclerAdapter<Products, OrderViewHolder> adapter =
                new FirebaseRecyclerAdapter<Products, OrderViewHolder>(options) {
            @NonNull
            @Override
            public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_detail_layout, parent, false);
                return new OrderViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull OrderViewHolder holder, int position, @NonNull final Products model) {
                int qty = Integer.parseInt(model.getQuantity());
                int price = Integer.parseInt(model.getPrice());
                holder.txtProductName.setText(qty+ " x " + model.getPname());
                Picasso.get().load(model.getImage()).into(holder.imageView);
                holder.txtProductPrice.setText(String.valueOf(qty*price));
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }


}
