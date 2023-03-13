package com.goblin.qrhunter.ui.search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.goblin.qrhunter.Player;
import com.goblin.qrhunter.R;

import java.util.List;

public class playerSearchAdapter extends FirestoreRecyclerAdapter<Player, playerSearchAdapter.playerViewholder> {
    public playerSearchAdapter(@NonNull FirestoreRecyclerOptions<Player> options){
        super(options);
    }
    // Bind view to the card view (player_search_content.xml) with the "player.java" class.
    @Override
    protected void
    onBindViewHolder(@NonNull playerViewholder holder,
                     int position, @NonNull Player model)
    {

        holder.username.setText(model.getUsername());
        holder.email.setText(model.getContactInfo());
        holder.phone.setText(model.getPhone());
    }

    // Function to tell the class about the Card view (what data to be shown)
    @NonNull
    @Override
    public playerViewholder
    onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.player_search_content, parent, false);
        return new playerSearchAdapter.playerViewholder(view);
    }

    // Sub Class to create references of the views.
    class playerViewholder extends RecyclerView.ViewHolder {
        TextView username, email, phone;
        public playerViewholder(@NonNull View itemView)
        {
            super(itemView);

            username = itemView.findViewById(R.id.searched_username);
            email = itemView.findViewById(R.id.result_email);
            phone = itemView.findViewById(R.id.result_phone);
        }
    }
}