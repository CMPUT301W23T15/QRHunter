package com.goblin.qrhunter.ui.summary;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.goblin.qrhunter.Post;
import com.goblin.qrhunter.R;

import java.util.ArrayList;
import java.util.List;

public class QRRecyclerAdapter extends RecyclerView.Adapter<QRRecyclerAdapter.QRViewHolder> {

    private List<Post> mPosts = new ArrayList<>();


    public QRRecyclerAdapter(List<Post> posts) {
        mPosts = posts;
    }

    @NonNull
    @Override
    public QRViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_qr_list_item, parent, false);
        return new QRViewHolder(view);
    }

    @Override
    public void onBindViewHolder(QRViewHolder holder, int position) {
        Post post = mPosts.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    public void setPosts(List<Post> posts) {
        mPosts = posts;
    }


    public static class QRViewHolder extends RecyclerView.ViewHolder {

        private TextView mTitle;
        private TextView mPoints;

        public QRViewHolder(View itemView) {
            super(itemView);

            mTitle = itemView.findViewById(R.id.qr_list_item_title);
            mPoints = itemView.findViewById(R.id.qr_list_item_points);
        }

        public void bind(Post post) {
            mTitle.setText(post.getName());
            mPoints.setText(post.getCode().getScore());
        }
    }
}