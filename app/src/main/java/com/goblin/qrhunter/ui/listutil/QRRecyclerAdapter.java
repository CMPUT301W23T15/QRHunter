/**
 * A RecyclerView adapter for displaying a list of QR codes in the OtherProfileFragment.
 * This adapter receives a list of Post objects and uses them to populate the list of
 * QR codes in the RecyclerView. The adapter also implements an inner ViewHolder class
 * for defining the view of each individual QR code item.
 */
package com.goblin.qrhunter.ui.listutil;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.goblin.qrhunter.Post;
import com.goblin.qrhunter.R;
import com.goblin.qrhunter.ui.post.PostFragment;

import java.util.ArrayList;
import java.util.List;

public class QRRecyclerAdapter extends RecyclerView.Adapter<QRRecyclerAdapter.QRViewHolder> {

    List<Post> mPosts = new ArrayList<>();
    String TAG = "QRRecyclerAdapter";


    /**
     * set the underlying data of the lists of posts
     * @param posts post of the user to display.
     */
    public void setData(List<Post> posts) {
        if (posts != null) {
            mPosts.clear();
            mPosts.addAll(posts);
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
    public QRViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.qr_code_content, parent, false);
        return new QRViewHolder(view);
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     *
     * @param holder the ViewHolder which should be updated
     * @param position the position of the item within the adapter's data set
     */
    @Override
    public void onBindViewHolder(QRViewHolder holder, int position) {
        Post post = mPosts.get(position);
        holder.bind(post);
        holder.itemView.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable(PostFragment.POST_FRAGMENT_POST_KEY, post);
            Navigation.findNavController(v).navigate(R.id.action_global_postFragment, bundle);
        });
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return the total number of items
     */
    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    /**
     * ViewHolder class for defining the view of each individual QR code item in the RecyclerView.
     */
    public static class QRViewHolder extends RecyclerView.ViewHolder {

        String TAG = "QRRecyclerAdapter";

        private TextView mTitle;
        private TextView mPoints;



        /**
         * Constructs a new instance of QRViewHolder with the given view as the item view.
         *
         * @param itemView the view of the QR code item
         */
        public QRViewHolder(View itemView) {
            super(itemView);

            mTitle = itemView.findViewById(R.id.qr_name);
            mPoints = itemView.findViewById(R.id.qr_points);
        }

        /**
         * Binds the given Post object to the views in the QRViewHolder.
         *
         * @param post the Post object to bind
         */
        public void bind(Post post) {
            mTitle.setText(post.getName());
            Log.d(TAG, "bind: " + post.getName());
            mPoints.setText(String.valueOf(post.getCode().getScore()));
        }
    }
}