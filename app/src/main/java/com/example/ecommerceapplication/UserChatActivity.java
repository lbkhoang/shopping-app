package com.example.ecommerceapplication;

import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
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

        floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserChatActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });


        Toolbar toolbar = findViewById(R.id.bottom_app_bar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

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
                holder.txtUserName.setText(model.getName());
                //Picasso.get().load("https://imgur.com/gallery/YZlcj").into(holder.imageView);
                holder.txtUserDescription.setText("2.5 ★");
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(UserChatActivity.this, ChatActivity.class);
                        intent.putExtra("userId", model.getPhone());
                        startActivity(intent);
                    }
                });
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}
