package com.goblin.qrhunter.ui.listutil;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.goblin.qrhunter.Comment;
import com.goblin.qrhunter.R;
import com.goblin.qrhunter.data.CommentRepository;
import com.goblin.qrhunter.data.PostRepository;

import java.util.ArrayList;
import java.util.List;



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
        holder.bind(comment);
        String playerID = comment.getPlayerId();
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

        /**
         * Constructs a new instance of commentViewHolder with the given view as the item view.
         *
         * @param itemView the view of the comment
         */
        public commentViewHolder(View itemView) {
            super(itemView);
            mCommentNameView = itemView.findViewById(R.id.username_text_view);
        }

        /**
         * Binds the given comment object to the views in the commentViewHolder.
         *
         * @param comment the comment object to bind
         */
        public void bind(Comment comment) {
            mCommentNameView.setText(comment.getText());
        }
    }
}