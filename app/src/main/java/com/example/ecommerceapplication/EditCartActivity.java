package com.example.ecommerceapplication;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ecommerceapplication.Model.Products;
import com.example.ecommerceapplication.Model.Users;
import com.example.ecommerceapplication.ViewHolder.OrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.*;
import com.squareup.picasso.Picasso;
import io.paperdb.Paper;


public class EditCartActivity extends AppCompatActivity {

    private DatabaseReference OrderRef;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Button checkOutButton;
    private TextView txtTotalPrice;
    private Users user;
    private int totalPrice = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_cart);

        checkOutButton = findViewById(R.id.check_out_btn);
        txtTotalPrice = findViewById(R.id.txtTotal);

        Paper.init(this);
        user = Paper.book().read("userDetail");

        OrderRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(user.getPhone());
        recyclerView = findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        checkOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkout();
            }
        });
        setData();
    }


    protected void setData() {

        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(OrderRef, Products.class)
                        .build();

        FirebaseRecyclerAdapter<Products, OrderViewHolder> adapter =
                new FirebaseRecyclerAdapter<Products, OrderViewHolder>(options) {
                    @NonNull
                    @Override
                    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout, parent, false);
                        return new OrderViewHolder(view);
                    }

                    @Override
                    protected void onBindViewHolder(@NonNull OrderViewHolder holder, int position, @NonNull final Products model) {
                        holder.txtProductName.setText(model.getPname());
                        holder.txtProductPrice.setText("Price = " + model.getPrice() + "$");
                        holder.txtProductQuantity.setText("Amount = " +model.getQuantity());

                        Picasso.get().load(model.getImage()).into(holder.imageView);
                        holder.imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                    Intent intent = new Intent(EditCartActivity.this, AddToCartActivity.class);
                                    intent.putExtra("pId", model.getPid());
                                    intent.putExtra("amount", model.getQuantity());
                                    startActivity(intent);
                                }
                            });
                        }
                    };

        recyclerView.setAdapter(adapter);
        adapter.startListening();

        OrderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                totalPrice = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Products product = snapshot.getValue(Products.class);
                    int productTotalCost = Integer.parseInt(product.getPrice()) * Integer.parseInt(product.getQuantity());
                    totalPrice = totalPrice + productTotalCost;
                    txtTotalPrice.setText("Total: " + totalPrice + "$");
                }
                if (!dataSnapshot.exists()) {
                    txtTotalPrice.setText("nothing");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void checkout() {
        if ("nothing".equals(txtTotalPrice.getText().toString())) {
            Toast.makeText(this, "Nothing in Cart", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(EditCartActivity.this, CheckOutActivity.class);
            intent.putExtra("amount", txtTotalPrice.getText());
            startActivity(intent);
        }
    }



}
