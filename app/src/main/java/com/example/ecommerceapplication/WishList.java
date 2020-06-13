package com.example.ecommerceapplication;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ecommerceapplication.Model.Products;
import com.example.ecommerceapplication.Model.Users;
import com.example.ecommerceapplication.ViewHolder.ProductViewHolder;
import com.example.ecommerceapplication.ViewHolder.WishListViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.*;
import com.squareup.picasso.Picasso;
import io.paperdb.Paper;

import java.util.HashMap;
import java.util.Map;

public class WishList extends AppCompatActivity {

    private DatabaseReference ProductsRef;
    private RecyclerView recyclerView;
    private Users user;
    private DatabaseReference FavRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish_list);

        Paper.init(this);
        user = Paper.book().read("userDetail");
        ProductsRef = FirebaseDatabase.getInstance().getReference().child("user").child(user.getPhone()).child("favorite");
        //TODO update role in db
        if (user.getRole() == null) {
            user.setRole("");
        }

        FloatingActionButton floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WishList.this, EditCartActivity.class);
                startActivity(intent);
            }
        });


        Toolbar toolbar = findViewById(R.id.bottom_app_bar);
        setSupportActionBar(toolbar);

        //item layout
        recyclerView = findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        setProductData();
    }

    private void setProductData() {

        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(ProductsRef, Products.class)
                        .build();


        FirebaseRecyclerAdapter<Products, WishListViewHolder> adapter =
                new FirebaseRecyclerAdapter<Products, WishListViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull WishListViewHolder holder, int position, @NonNull final Products model) {
                        holder.txtProductName.setText(model.getPname());
                        //holder.txtProductDescription.setText(model.getDescription());
                        holder.txtProductPrice.setText("Price = " + model.getPrice() + "$");
                        Picasso.get().load(model.getImage()).into(holder.imageView);
                        holder.imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                checkProduct(model);
                                if (user.getRole().equals("Admin")){

                                    Intent intent = new Intent(WishList.this, EditActivity.class);
                                    intent.putExtra("pId", model.getPid());
                                    startActivity(intent);
                                } else {
                                    Intent intent = new Intent(WishList.this, AddToCartActivity.class);
                                    intent.putExtra("pId", model.getPid());
                                    startActivity(intent);
                                }
                            }
                        });
                        holder.imageButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                removeFromWishList(model.getPid());
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public WishListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_wish_list, parent, false);
                        return new WishListViewHolder(view);
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    private void removeFromWishList(final String pId) {

        FavRef = FirebaseDatabase.getInstance().getReference().child("user").child(user.getPhone()).child("favorite");
        FavRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    dataSnapshot.child(pId).getRef().removeValue();
                    Toast.makeText(WishList.this, "removed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void checkProduct(Products productFav){
        final String productFavId = productFav.getPid();
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("Products").child(productFavId);
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Products product = dataSnapshot.getValue(Products.class);
                    Map<String, Object> data = new HashMap<>();
                    data.put(productFavId, product);
                    ProductsRef.child(productFavId).updateChildren(data);
                } else {
                    ProductsRef.child(productFavId).removeValue();
                    //TODO intent here
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
