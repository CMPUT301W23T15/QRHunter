package com.goblin.qrhunter.ui.listutil;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.goblin.qrhunter.Comment;
import com.goblin.qrhunter.R;

import java.util.ArrayList;
import java.util.List;



public class commentRecyclerAdapter extends RecyclerView.Adapter<commentRecyclerAdapter.commentViewHolder> {

    List<Comment> mComments = new ArrayList<>();
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