package com.example.ecommerceapplication;

import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.*;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ecommerceapplication.Model.Users;
import com.example.ecommerceapplication.ViewHolder.OrderViewHolder;
import com.example.ecommerceapplication.ViewHolder.UserViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.*;
import com.squareup.picasso.Picasso;
import io.paperdb.Paper;

public class UserChatActivity extends AppCompatActivity {

    private DatabaseReference userRef;
    private RecyclerView recyclerView;
    private Users user;
    private FloatingActionButton floatingActionButton;
    private RecyclerView.LayoutManager layoutManager;
    private Query userQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_chat);

        userRef = FirebaseDatabase.getInstance().getReference().child("user");

        userQuery = userRef.orderByChild("role")
        .startAt("Admin")
        .endAt("Admin");

        Paper.init(this);
        user = Paper.book().read("userDetail");

        recyclerView = findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        setBottomNavBar();

        loadUserData();


    }

    private void loadUserData() {
        FirebaseRecyclerOptions<Users> options =
                new FirebaseRecyclerOptions.Builder<Users>()
                        .setQuery(userRef, Users.class)
                        .build();

        FirebaseRecyclerAdapter<Users, UserViewHolder> adapter = new FirebaseRecyclerAdapter<Users, UserViewHolder>(options) {
            @NonNull
            @Override
            public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_layout, parent, false);
                return new UserViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull UserViewHolder holder, int position, @NonNull final Users model) {
                final Intent intent = new Intent(UserChatActivity.this, ChatActivity.class);
                String img = model.getImage();
                intent.putExtra("userId", model.getPhone())
                    .putExtra("name", model.getName());
                String des = model.getDescription();
                if (img != null) {
                    intent.putExtra("img", img);
                    Picasso.get().load(img).into(holder.imageView);
                } else {
                    intent.putExtra("img", "");
                }

                if (des != null) {
                    holder.txtUserDescription.setText(des);
                    intent.putExtra("des", des);
                } else {
                    holder.txtUserDescription.setText("");
                    intent.putExtra("des", "");
                }
                holder.txtUserName.setText(model.getName());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(intent);
                    }
                });
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    private void setBottomNavBar() {

        FloatingActionButton floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserChatActivity.this, EditCartActivity.class);
                startActivity(intent);
            }
        });


        Toolbar toolbar = findViewById(R.id.bottom_app_bar);
        setSupportActionBar(toolbar);

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference()
                .child("Orders").child(user.getPhone());

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                TextView textView = findViewById(R.id.notification_badge);
                textView.setText(String.valueOf(dataSnapshot.getChildrenCount()));
                Log.d("main", "onDataChange: " + dataSnapshot.getChildrenCount());
                if (!dataSnapshot.exists()){
                    textView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
                intent = new Intent(UserChatActivity.this, UserChatActivity.class);
                startActivity(intent);
                return true;
            case R.id.cart_app_bar:
                intent = new Intent(UserChatActivity.this, WishList.class);
                startActivity(intent);
                return true;
            case R.id.order_app_bar:
                intent = new Intent(UserChatActivity.this, OrderListActivity.class);
                startActivity(intent);
                return true;
            case R.id.search_app_bar:
                intent = new Intent(UserChatActivity.this, Search.class);
                startActivity(intent);
                return true;
            case R.id.profile_app_bar:
                intent = new Intent(UserChatActivity.this, ProfileActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
