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

/**
 * A custom adapter for displaying player data in a leaderboard view.
 */
public class PlayerAdapter extends BaseAdapter {
    private Context context;
    private List<Player> playerList;
    private String mode = "score";

    /**
     * Constructor for creating a new PlayerAdapter object.
     *
     * @param context The context in which the adapter is being used.
     * @param playerList The list of players to be displayed in the leaderboard.
     */
    public PlayerAdapter(Context context, List<Player> playerList) {
        this.context = context;
        this.playerList = playerList;
    }

    /**
     * Constructor for creating a new PlayerAdapter object with a specified mode.
     *
     * @param context The context in which the adapter is being used.
     * @param playerList The list of players to be displayed in the leaderboard.
     * @param mode The mode of the leaderboard i.e. score or amount
     */
    public PlayerAdapter(Context context, List<Player> playerList, String mode) {
        this.context = context;
        this.playerList = playerList;
        this.mode = mode;
    }

    /**
     * Returns the number of players in the leaderboard.
     *
     * @return the number of players in the leaderboard.
     */
    @Override
    public int getCount() {
        return playerList.size();
    }

    /**
     * Returns the player at the specified position in the leaderboard.
     *
     * @param position The position of the player in the leaderboard.
     *
     * @return the player at the specified position in the leaderboard.
     */
    @Override
    public Player getItem(int position) {
        return playerList.get(position);
    }

    /**
     * Returns the ID of the player at the specified position in the leaderboard.
     *
     * @param position position The position of the player in the leaderboard.
     *
     * @return the ID of the player at the specified position in the leaderboard.
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Returns the view for the player at the specified position in the leaderboard.
     *
     * @param position position The position of the player in the leaderboard.
     * @param convertView convertView The old view to reuse, if possible.
     * @param parent The parent view that the new view will be attached to.
     *
     * @return the view for the player at the specified position in the leaderboard.
     */
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
        if (mode.equals("amount")) {
            TextView scoreTitleTextView = view.findViewById(R.id.player_score_text);
            scoreTitleTextView.setText("Total Amount:");
        }
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
