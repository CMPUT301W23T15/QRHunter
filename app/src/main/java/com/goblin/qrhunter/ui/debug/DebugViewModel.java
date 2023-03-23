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

public class DebugViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private String TAG = "DebugViewModel";

    private PostRepository postDB;

    public DebugViewModel() {
        postDB = new PostRepository();
    }

    public Task<Void> generatePost() {
        FirebaseUser usr = FirebaseAuth.getInstance().getCurrentUser();
        if(usr == null) {
            Log.d(TAG, "generatePosts: no loggedin user");
            return Tasks.forCanceled();
        }
        String usrId = usr.getUid();
        String rand = UUID.randomUUID().toString();
        QRCode code = new QRCode(rand);
        Post p1 = new Post(rand, code, usrId);

         return postDB.add(p1);
    }

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