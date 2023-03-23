package com.goblin.qrhunter.ui.also;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.goblin.qrhunter.Player;
import com.goblin.qrhunter.R;
import com.goblin.qrhunter.Score;

import java.util.ArrayList;
import java.util.List;

public class AlsoRecyclerAdapter extends RecyclerView.Adapter<AlsoRecyclerAdapter.AlsoViewHolder> {

    List<Score> mScores = new ArrayList<>();
    String TAG = "";


    /**
     * set the underlying data of the lists of posts
     * @param scores list of scores which contain the user to display.
     */
    public void setData(List<Score> scores) {
        Log.d(TAG, "setData: called " + String.valueOf(scores.size()));
        if (scores != null) {
            mScores.clear();
            mScores.addAll(scores);
            notifyDataSetChanged();
        }
    }

    /**
     * Called when RecyclerView needs a new ViewHolder of the given type to represent an item.
     *
     * @param parent the parent view group
     * @param viewType the type of the view
     * @return a new QRViewHolder instance
     */
    @NonNull
    @Override
    public AlsoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.also_content, parent, false);
        return new AlsoViewHolder(view);
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     *
     * @param holder the ViewHolder which should be updated
     * @param position the position of the item within the adapter's data set
     */
    @Override
    public void onBindViewHolder(AlsoViewHolder holder, int position) {
        Score score = mScores.get(position);
        holder.bind(score);
        holder.itemView.setOnClickListener(v -> {
            // TODO: navigate to player profile
            /*
            Bundle bundle = new Bundle();
            bundle.putSerializable(PostFragment.POST_FRAGMENT_POST_KEY, score);
            Navigation.findNavController(v).navigate(R.id.action_global_postFragment, bundle);
             */
        });
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return the total number of items
     */
    @Override
    public int getItemCount() {
        return mScores.size();
    }

    /**
     * ViewHolder class for defining the view of each individual QR code item in the RecyclerView.
     */
    public static class AlsoViewHolder extends RecyclerView.ViewHolder {

        String TAG = "AlsoViewHolder";

        TextView usernameTxtView;


        /**
         * Constructs a new instance of QRViewHolder with the given view as the item view.
         *
         * @param itemView the view of the QR code item
         */
        public AlsoViewHolder(View itemView) {
            super(itemView);
            usernameTxtView = itemView.findViewById(R.id.username_text_view);

        }

        /**
         * Binds the given Post object to the views in the QRViewHolder.
         *
         * @param score the Post object to bind
         */
        public void bind(Score score) {
            if(score == null) {
                return;
            }

            Player player = score.getPlayer();
            if( player == null || player.getUsername() == null) {
                usernameTxtView.setText("Unknown");
                return;
            }

            usernameTxtView.setText(player.getUsername());
            Log.d(TAG, "bind: " + player.getUsername());

        }
    }
}