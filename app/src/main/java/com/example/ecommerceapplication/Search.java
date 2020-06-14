package com.example.ecommerceapplication;

import android.content.Intent;
import android.view.*;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ecommerceapplication.Model.Products;
import com.example.ecommerceapplication.Model.Users;
import com.example.ecommerceapplication.ViewHolder.OrderViewHolder;
import com.example.ecommerceapplication.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;
import io.paperdb.Paper;

public class Search extends AppCompatActivity {

    private DatabaseReference ProductsRef;
    private RecyclerView recyclerView;
    private Users user;
    private Query productQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");
        productQuery = ProductsRef.orderByChild("category")
        .startAt("Female Dresses")
        .endAt("Female Dresses");
        Paper.init(this);
        user = Paper.book().read("userDetail");
        //TODO update role in db
        if (user.getRole() == null) {
            user.setRole("");
        }

        setBtn();

        FloatingActionButton floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Search.this, EditCartActivity.class);
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

    }

    private Query setQuery(String query) {
        return productQuery = ProductsRef.orderByChild("category").startAt(query).endAt(query);
    }

    private void setBtn() {
        //drinks
        ImageView coffee = (ImageView) findViewById(R.id.coffee);
        ImageView iceBlend = (ImageView) findViewById(R.id.ice_blend);
        ImageView softDrink = (ImageView) findViewById(R.id.soft_drink);
        ImageView water = (ImageView) findViewById(R.id.water);

        //deserts
        ImageView cake = (ImageView) findViewById(R.id.cake);
        ImageView cupcake = (ImageView) findViewById(R.id.cupcake);
        ImageView cookie = (ImageView) findViewById(R.id.cookie);
        ImageView bread = (ImageView) findViewById(R.id.bread);

        coffee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productQuery = setQuery("coffee");
                setProductData(productQuery);
            }
        });


        iceBlend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productQuery = setQuery("iceBlend");
                setProductData(productQuery);
            }
        });


        softDrink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productQuery = setQuery("softDrink");
                setProductData(productQuery);
            }
        });


        water.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productQuery = setQuery("water");
                setProductData(productQuery);
            }
        });


        cake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productQuery = setQuery("cake");
                setProductData(productQuery);
            }
        });


        cupcake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productQuery = setQuery("cupcake");
                setProductData(productQuery);
            }
        });


        cookie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productQuery = setQuery("cookie");
                setProductData(productQuery);
            }
        });


        bread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productQuery = setQuery("bread");
                setProductData(productQuery);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        setProductData(ProductsRef);
    }
    private void setProductData(Query productQuery) {

        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(productQuery, Products.class)
                        .build();


        FirebaseRecyclerAdapter<Products, OrderViewHolder> adapter =
                new FirebaseRecyclerAdapter<Products, OrderViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull OrderViewHolder holder, int position, @NonNull final Products model) {
                        holder.txtProductName.setText(model.getPname());
                        //holder.txtProductDescription.setText(model.getDescription());
                        holder.txtProductPrice.setText("Price = " + model.getPrice() + "$");
                        Picasso.get().load(model.getImage()).into(holder.imageView);
                        holder.imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent;
                                if (user.getRole().equals("Admin")){
                                    intent = new Intent(Search.this, EditActivity.class);
                                } else {
                                    intent = new Intent(Search.this, AddToCartActivity.class);
                                }
                                intent.putExtra("pId", model.getPid());
                                startActivity(intent);

                            }
                        });
                    }

                    @NonNull
                    @Override
                    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout, parent, false);
                        return new OrderViewHolder(view);
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()){
            case R.id.home_app_bar:
                intent = new Intent(Search.this, UserChatActivity.class);
                startActivity(intent);
                return true;
            case R.id.cart_app_bar:
                showMsg("cart");
                return true;
            case R.id.order_app_bar:
                intent = new Intent(Search.this, OrderListActivity.class);
                startActivity(intent);
                return true;
            case R.id.search_app_bar:
                showMsg("search");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showMsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

}


