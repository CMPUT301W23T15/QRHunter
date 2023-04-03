package com.goblin.qrhunter.ui.listutil;

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.goblin.qrhunter.Comment;
import com.goblin.qrhunter.Player;
import com.goblin.qrhunter.R;
import com.goblin.qrhunter.data.CommentRepository;
import com.goblin.qrhunter.data.PlayerRepository;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.common.returnsreceiver.qual.This;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Custom adapter which is used to display comments
 */
public class commentRecyclerAdapter extends RecyclerView.Adapter<commentRecyclerAdapter.commentViewHolder> {

    List<Comment> mComments = new ArrayList<>();
    CommentRepository commentDB;
    private final String TAG = "commentRecyclerAdapter";


    /**
     * set the underlying data of the lists of comments
     * @param comments post of the user to display.
     */
    public void setValue(List<Comment> comments) {
        if (comments != null) {
            mComments.clear();
            mComments.addAll(comments);
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
    public commentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_content, parent, false);
        return new commentViewHolder(view);
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     *
     * @param holder the ViewHolder which should be updated
     * @param position the position of the item within the adapter's data set
     */
    @Override
    public void onBindViewHolder(commentViewHolder holder, int position) {
        Comment comment = mComments.get(position);
        String playerID = comment.getPlayerId();
        holder.bind(comment, comment.getPlayerId()); // Binds the comment, and the user ID of the commenter to that post.

        // Check if the current player ID actually left that comment.
        // If so, they get access to a pop-up menu that can delete the comment.
        if (playerID.equals(comment.getPlayerId())){
            PopupMenu popupMenu = new PopupMenu(holder.itemView.getContext(), holder.itemView);
            popupMenu.getMenuInflater().inflate(R.menu.long_press_comment_menu, popupMenu.getMenu());
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    PopupMenu popupMenu = new PopupMenu(v.getContext(), v);

                    popupMenu.inflate(R.menu.long_press_comment_menu);
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            // Handle menu item click
                            switch (item.getItemId()) {
                                case R.id.delete_comment:
                                    commentDB = new CommentRepository();
                                    commentDB.delete(comment.getId());
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
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return the total number of items
     */
    @Override
    public int getItemCount() {
        return mComments.size();
    }

    /**
     * ViewHolder class for defining the view of each individual comment comment item in the RecyclerView.
     */
    public static class commentViewHolder extends RecyclerView.ViewHolder {

       private final String TAG = "commentViewHolder";
       private final TextView mCommentNameView;
       private final TextView mCommenterView;
       private final ImageView mProfileImageView;

       private static FirebaseFirestore db = FirebaseFirestore.getInstance();

        /**
         * Fetches the username of the player with the given ID and passes it to the provided listener.
         * @param playerId the ID of the player whose username needs to be fetched
         * @param listener the listener that will receive the fetched username
         */
       private void fetchUsername(String playerId, final OnUsernameFetchedListener listener) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference docRef = db.collection("players").document(playerId);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String username = document.getString("username");
                            listener.onUsernameFetched(username);
                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });
        }

        /**
         * Interface for listening to the fetching of a username.
         */
        interface OnUsernameFetchedListener {
            /**
             * Called when the username is fetched successfully.
             * @param username the username of the player
             */
            void onUsernameFetched(String username);
        }

        /**
         * Constructs a new instance of commentViewHolder with the given view as the item view.
         *
         * @param itemView the view of the comment
         */
        public commentViewHolder(View itemView) {
            super(itemView);
            mCommentNameView = itemView.findViewById(R.id.user_comment);
            mCommenterView = itemView.findViewById(R.id.commenter);
            mProfileImageView = itemView.findViewById(R.id.player_icon_image);
        }

        /**
         * Binds the given comment object to the views in the commentViewHolder.
         * Sets the username + profile picture of a given comment.
         *
         * @param comment the comment object to bind
         */
        public void bind(Comment comment, String whoCommented) {

            fetchUsername(whoCommented, new OnUsernameFetchedListener() {
                @Override
                public void onUsernameFetched(String username) {
                    String encodedUsername = Uri.encode(username);
                    Glide.with(itemView.getContext())
                            .load("https://api.dicebear.com/6.x/avataaars/png?seed=" + encodedUsername)
                            .placeholder(R.drawable.ic_profile_24)
                            .into(mProfileImageView);

                    // Set username
                    mCommenterView.setText(username + ":");

                }
            });

            /* Once the username can be set to a display name. Uncomment below delete the line: "mWhoCommentedView.setText(whoCommented);"
            FirebaseUser usr = FirebaseAuth.getInstance().getCurrentUser();
            mWhoCommentedView.setText(usr.getDisplayName());
             */

            mCommentNameView.setText(comment.getText()); // Concatenate comment.getUsername() if it ends up working.
//            FirebaseFirestore db = FirebaseFirestore.getInstance();
//            DocumentReference docRef = db.collection("players").document(whoCommented);
//            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                    if (task.isSuccessful()) {
//                        DocumentSnapshot document = task.getResult();
//                        if (document.exists()) {
//                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
//                            // ADDS THE COMMENTER'S USERNAME TO THE COMMENT:
//                            mWhoCommentedView.setText(document.getString("username"));
//
//                        } else {
//                            Log.d(TAG, "No such document");
//                        }
//                    } else {
//                        Log.d(TAG, "get failed with ", task.getException());
//                    }
//                }
//            });
        }
    }
}