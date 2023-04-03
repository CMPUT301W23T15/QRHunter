package com.goblin.qrhunter.ui.debug;


import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.goblin.qrhunter.Post;
import com.goblin.qrhunter.QRCode;
import com.goblin.qrhunter.data.PostRepository;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.UUID;

/**
 * ViewModel class for the Debug screen.
 * Provides functionality for generating random and fixed posts.
 */
public class DebugViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private String TAG = "DebugViewModel";

    private PostRepository postDB;

    /**
     * Constructor for DebugViewModel.
     * Initializes the PostRepository instance.
     */
    public DebugViewModel() {
        postDB = new PostRepository();
    }

    /**
     * Generates a random Post with a random QRCode and location,
     * and adds it to the PostRepository.
     *
     * @return A Task representing the asynchronous operation of adding the post else
     * a cancelled task if no user is logged in
     */
    public Task<Void> generatePost() {
        FirebaseUser usr = FirebaseAuth.getInstance().getCurrentUser();
        if(usr == null) {
            Log.d(TAG, "generatePosts: no loggedin user");
            return Tasks.forCanceled();
        }
        String usrId = usr.getUid();
        String rand = UUID.randomUUID().toString();
        QRCode code = new QRCode(rand);
        Post p1 = new Post("", code, usrId);
        double yegLat = 53.5461;
        double yegLng = -113.323975;
        double locLat = yegLat - 2 * Math.random();
        double locLng = yegLng - 2 * Math.random();
        p1.setLat(locLat);
        p1.setLng(locLng);
        Log.d(TAG, "generatePost: created post with usrId "+ usrId);
         return postDB.add(p1);
    }

    /**
     * Generates a fixed Post with a fixed QRCode string "hello",
     * and adds it to the PostRepository.
     *
     * @return A Task representing the asynchronous operation of adding the post else
     * a cancelled task if no user is logged in
     */
    public Task<Void> generateFixedPost() {
        FirebaseUser usr = FirebaseAuth.getInstance().getCurrentUser();
        if(usr == null) {
            Log.d(TAG, "generatePosts: no loggedin user");
            return Tasks.forCanceled();
        }
        String usrId = usr.getUid();
        String rand = "hello";
        QRCode code = new QRCode(rand);
        Post p1 = new Post(rand, code, usrId);

        return postDB.add(p1);
    }

}