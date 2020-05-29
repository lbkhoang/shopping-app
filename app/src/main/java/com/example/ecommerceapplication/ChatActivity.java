package com.example.ecommerceapplication;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ecommerceapplication.Model.Chats;
import com.example.ecommerceapplication.Model.Products;
import com.example.ecommerceapplication.Model.Users;
import com.example.ecommerceapplication.ViewHolder.ChatViewHolder;
import com.example.ecommerceapplication.ViewHolder.OrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import io.paperdb.Paper;

public class ChatActivity extends AppCompatActivity {

    private DatabaseReference ChatsRef;
    private Users user;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        
        Paper.init(this);
        user = Paper.book().read("userDetail");
        
        //TODO get chat id by combine 2 user id
        ChatsRef = FirebaseDatabase.getInstance().getReference().child("Chats").child("test");
        
        recyclerView = findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        
        loadChatData();

        Button sendBtn = findViewById(R.id.send_btn);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
        
    }


    private void loadChatData() {
        FirebaseRecyclerOptions<Chats> options =
                new FirebaseRecyclerOptions.Builder<Chats>()
                        .setQuery(ChatsRef, Chats.class)
                        .build();

        FirebaseRecyclerAdapter<Chats, ChatViewHolder> adapter = new FirebaseRecyclerAdapter<Chats, ChatViewHolder>(options) {
            @NonNull
            @Override
            public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_bubble, parent, false);
                return new ChatViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull ChatViewHolder holder, int position, @NonNull final Chats model) {
                holder.txtUserName.setText(model.getUserName());
                holder.txtUserChat.setText(model.getMessage());
            }
        };
        //scroll to last
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                    recyclerView.scrollToPosition(positionStart);
            }
        });

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    private void sendMessage() {
        EditText chatInput = findViewById(R.id.txtChatInput);
        String message = chatInput.getText().toString();

        Chats chats = new Chats(user.getName(), message);
        if  (!"".equals(message)) {
            ChatsRef.push().setValue(chats);
        }

        chatInput.setText("");
        if (recyclerView.getAdapter() != null){
            int itemCount = recyclerView.getAdapter().getItemCount();
            recyclerView.scrollToPosition(itemCount-1);
        }
    }
}
