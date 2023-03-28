package com.goblin.qrhunter.ui.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;


import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.goblin.qrhunter.Player;
import com.goblin.qrhunter.R;
import com.goblin.qrhunter.ui.profile.OtherProfileFragment;

import java.util.List;

public class playerSearchAdapter extends FirestoreRecyclerAdapter<Player, playerSearchAdapter.playerViewholder> {
    public playerSearchAdapter(@NonNull FirestoreRecyclerOptions<Player> options){
        super(options);
    }

    /**
     * Binds view to the card view (player_search_content.xml) with the "player.java" class.
     * @param holder where the data for a given user is stored.
     * @param position not used.
     * @param model the model object containing the data that should be used to populate the view.
     */
    @Override
    protected void
    onBindViewHolder(@NonNull playerViewholder holder,
                     int position, @NonNull Player model)
    {
        String playerId = model.getId();
        String username = model.getUsername();
        String contactInfo = model.getContactInfo();
        holder.username.setText(model.getUsername());
        holder.email.setText(model.getContactInfo());
        holder.phone.setText(model.getPhone());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString(OtherProfileFragment.PLAYER_USERNAME_ARG_KEY, model.getUsername());
                bundle.putString(OtherProfileFragment.PLAYER_PHONE_ARG_KEY, model.getPhone());
                bundle.putString(OtherProfileFragment.PLAYER_CONTACT_INFO_ARG_KEY, model.getContactInfo());
                bundle.putSerializable(OtherProfileFragment.PLAYER_ARG_KEY, model);

                Navigation.findNavController(v).navigate(R.id.action_search_select_player, bundle);
            }
        });
        // holder.phone.setText(model.getContactInfo();
    }

    /**
     * Tells the class about the Card view (what data to be shown)
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return
     */
    @NonNull
    @Override
    public playerViewholder
    onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.player_search_content, parent, false);
        return new playerSearchAdapter.playerViewholder(view);
    }

    /**
     * Sub Class to create references of the views.
     */
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
