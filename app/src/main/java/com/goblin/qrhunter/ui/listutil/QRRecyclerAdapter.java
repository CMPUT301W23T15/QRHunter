/**
 * A RecyclerView adapter for displaying a list of QR codes in the OtherProfileFragment.
 * This adapter receives a list of Post objects and uses them to populate the list of
 * QR codes in the RecyclerView. The adapter also implements an inner ViewHolder class
 * for defining the view of each individual QR code item.
 */
package com.goblin.qrhunter.ui.listutil;

import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.goblin.qrhunter.Post;
import com.goblin.qrhunter.R;
import com.goblin.qrhunter.data.PostRepository;
import com.goblin.qrhunter.ui.post.PostFragment;
import com.goblin.qrhunter.ui.summary.SummaryFragment;

import java.util.ArrayList;
import java.util.List;

public class QRRecyclerAdapter extends RecyclerView.Adapter<QRRecyclerAdapter.QRViewHolder> {

    List<Post> mPosts = new ArrayList<>();
    String TAG = "QRRecyclerAdapter";
    PostRepository postDB;


    /**
     * set the underlying data of the lists of posts
     *
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
     * @param parent   the parent view group
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
     * @param holder   the ViewHolder which should be updated
     * @param position the position of the item within the adapter's data set
     */
    @Override
    public void onBindViewHolder(QRViewHolder holder, int position) {
        // "post" = Will always be the post that you click.
        Post post = mPosts.get(position);
        holder.bind(post);

        // Set-up pop-up menu.
        PopupMenu popupMenu = new PopupMenu(holder.itemView.getContext(), holder.itemView);
        popupMenu.getMenuInflater().inflate(R.menu.long_press_menu, popupMenu.getMenu());

        holder.itemView.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable(PostFragment.POST_FRAGMENT_POST_KEY, post);
            Navigation.findNavController(v).navigate(R.id.action_global_postFragment, bundle);
        });
        // Long-click: Bring up pop-up menu.
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                PopupMenu popupMenu = new PopupMenu(v.getContext(), v);

                popupMenu.inflate(R.menu.long_press_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        // Handle menu item click
                        switch (item.getItemId()) {
                            case R.id.edit_QR_code:
                                Toast.makeText(v.getContext(), "Edit option selected", Toast.LENGTH_SHORT).show();
                                return true;
                            case R.id.delete_QR_code:
                                Toast.makeText(v.getContext(), post.getName(), Toast.LENGTH_SHORT).show();
                                postDB = new PostRepository();
                                postDB.delete(post);

                                return true;
                            default:
                                return false;
                        }
                    }
                });
                popupMenu.show();
                return true;
            }
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
        private ImageView avatarImageView;

        /**
         * Constructs a new instance of QRViewHolder with the given view as the item view.
         *
         * @param itemView the view of the QR code item
         */
        public QRViewHolder(View itemView) {
            super(itemView);

            mTitle = itemView.findViewById(R.id.qr_name);
            mPoints = itemView.findViewById(R.id.qr_points);
            avatarImageView = itemView.findViewById(R.id.qr_list_item_icon);
        }

        /**
         * Binds the given Post object to the views in the QRViewHolder.
         *
         * @param post the Post object to bind
         */
        public void bind(Post post) {
            mTitle.setText(post.getName()); // mTitle is the name of the post.
            Log.d(TAG, "bind: " + post.getName());
            mPoints.setText(String.valueOf(post.getCode().getScore()));

            String hash = post.getCode().getHash();
            Glide.with(itemView)
                    .load("https://api.dicebear.com/6.x/bottts-neutral/png?seed=" + hash)
                    .placeholder(R.drawable.baseline_qr_code_50)
                    .into(avatarImageView);
        }
    }
}