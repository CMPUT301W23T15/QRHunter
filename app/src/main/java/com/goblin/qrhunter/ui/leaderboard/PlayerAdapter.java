package com.goblin.qrhunter.ui.leaderboard;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.goblin.qrhunter.Player;
import com.goblin.qrhunter.R;

import java.util.List;

public class PlayerAdapter extends BaseAdapter {
    private Context context;
    private List<Player> playerList;

    public PlayerAdapter(Context context, List<Player> playerList) {

        this.context = context;
        this.playerList = playerList;
    }
    @Override
    public int getCount() {
        return playerList.size();
    }

    @Override
    public Player getItem(int position) {
        return playerList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Player player = playerList.get(position);
        View view = convertView;
        // Get the player object for this position

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.player_ranking_content, null);
        }
        // Set the player's rank, name, and score in the view

        TextView rankTextView = view.findViewById(R.id.player_rank);
        TextView nameTextView = view.findViewById(R.id.player_name);
        TextView scoreTextView = view.findViewById(R.id.player_score);
        rankTextView.setText(Integer.toString(player.getRank()));
        nameTextView.setText(player.getUsername());
        scoreTextView.setText(Integer.toString(player.getTotalScore()));

        String encodedUsername = Uri.encode(player.getUsername());
        ImageView avatarImageView = view.findViewById(R.id.player_icon_image);

        Glide.with(context)
                .load("https://api.dicebear.com/6.x/avataaars/png?seed=" + encodedUsername)
                .placeholder(R.drawable.ic_profile_24)
                .into(avatarImageView);

        return view;
    }
}
