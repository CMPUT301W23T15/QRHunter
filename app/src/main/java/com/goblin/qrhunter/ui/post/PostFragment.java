package com.goblin.qrhunter.ui.post;


import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.goblin.qrhunter.Comment;
import com.goblin.qrhunter.Post;
import com.goblin.qrhunter.R;
import com.goblin.qrhunter.data.CommentRepository;
import com.goblin.qrhunter.databinding.FragmentPostBinding;
import com.goblin.qrhunter.ui.also.AlsoFragment;
import com.goblin.qrhunter.ui.listutil.commentRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.zip.Inflater;

public class PostFragment extends Fragment {

    static public String POST_FRAGMENT_POST_KEY = "POST_FRAGMENT_POST_KEY";
    private String TAG="PostFragment";
    private Post post;
    private PostViewModel vModel;
    private FragmentPostBinding binding;
    private CommentRepository commentDB;

    public static PostFragment newInstance(Post post) {
        PostFragment fragment = new PostFragment();
        fragment.post = post;
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentPostBinding.inflate(inflater, container, false);
        vModel = new ViewModelProvider(this).get(PostViewModel.class);
        if(post == null) {
            if(getArguments() == null) {
                return binding.getRoot();
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                this.post = getArguments().getSerializable(POST_FRAGMENT_POST_KEY, Post.class);
            }
        }

        assert this.post != null;

        FirebaseUser usr = FirebaseAuth.getInstance().getCurrentUser();

        commentDB = new CommentRepository();

        binding.postNameView.setText("Name: " + post.getName());
        // TODO: add location
        binding.postLocationView.setText("Location: ");

        commentRecyclerAdapter adapter = new commentRecyclerAdapter();
        binding.postCommentList.setAdapter(adapter);

        vModel.getComments(this.post.getId()).observe(getViewLifecycleOwner(), adapter::setValue);

        binding.addCommentBtn.setOnClickListener(v -> {
            Editable commentViewTxt = binding.addCommentView.getText();
            if(commentViewTxt == null || commentViewTxt.toString().length() < 2) {
                return;
            }
            String commentTxt = commentViewTxt.toString();
            Comment newComment = new Comment(usr.getUid(), post.getId(), commentTxt);
            commentDB.add(newComment).addOnCompleteListener(task -> {
                binding.addCommentBtn.setVisibility(View.INVISIBLE);
                if(!task.isSuccessful()) {
                    Toast.makeText(getContext(), "Failed to make comment", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "onCreateView: failed to make a comment", task.getException());
                }
                binding.addCommentView.setText("");
                binding.addCommentBtn.setVisibility(View.VISIBLE);
            });

        });

        binding.scannedByBtn.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString(AlsoFragment.ALSO_FRAGMENT_QR_ARG, this.post.getCode().getHash());
            Navigation.findNavController(v).navigate(R.id.action_global_alsoFragment, bundle);
        });
        return binding.getRoot();
    }


}