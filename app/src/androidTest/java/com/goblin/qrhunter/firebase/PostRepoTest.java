package com.goblin.qrhunter.firebase;
import com.goblin.qrhunter.data.PostRepository;

import com.goblin.qrhunter.Player;
import com.goblin.qrhunter.data.PlayerRepository;
import com.goblin.qrhunter.data.BaseRepository;
import androidx.lifecycle.LiveData;
//import static org.mockito.Mockito.*;
import org.junit.Test;
import org.junit.runner.RunWith;


// additionals
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import androidx.annotation.NonNull;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.goblin.qrhunter.data.PostRepository;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthSettings;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import org.junit.Before;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import com.goblin.qrhunter.Post;
import com.google.firebase.firestore.Query;
import com.goblin.qrhunter.QRCode;


/**
 * to run tests:
 * firebase:emulators:start
 *
 */

@RunWith(AndroidJUnit4.class)
public class PostRepoTest {
    PostRepository postDB;
    PlayerRepository playerDB;
    FirebaseFirestore fireStore;
    FirebaseAuth fireAuth;
    String playerId;

    FirebaseUser user;

    public void setUser(FirebaseUser user) {
        this.user = user;
    }

    /**
     * Setup firebase to use the emulated enviroment;
     */
    @Before
    public void init() throws ExecutionException, InterruptedException {
        fireStore = FirebaseFirestore.getInstance();
        fireAuth = FirebaseAuth.getInstance();
        try {
            // 10.0.2.2 is the special IP address to connect to the 'localhost' of
            // the host computer from an Android emulator.
            fireStore.useEmulator("10.0.2.2", 8080);
            fireAuth.useEmulator("10.0.2.2", 9099);

        } catch (IllegalStateException e) {
            // pass
        } catch (Exception e) {
            fail("failed to setup firebase emulator is firebase emulator running");
        }

        FirebaseFirestoreSettings firestoreSettings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(false)
                .build();
        fireStore.setFirestoreSettings(firestoreSettings);
        postDB = new PostRepository();
        playerDB = new PlayerRepository();
        playerId = addWithRandomHelper();
    }


    public String addWithRandomHelper() throws ExecutionException, InterruptedException {
        // generate random UUIDs simulates fireAuth.getCurrentUser()
        String playerId = UUID.randomUUID().toString();

        // a task to create a new user with random username
        Task<Void> createRandomUserTask = playerDB.addWithRandomUsername(playerId);
        // wait for task to finish
        Tasks.await(createRandomUserTask);

        // check if task failed
        if (!createRandomUserTask.isSuccessful()) {
            fail("addWithRandomHelper failed for unknown reason");
        }
        return playerId;
    }
    @Test
    public void addWithRandomTest() throws ExecutionException, InterruptedException {
        String playerId = addWithRandomHelper();
        if(playerId == null || playerId.isEmpty()) {
            fail("unknown failure");
        }
    }

    @Test
    public void addPostTest() {
        QRCode qr = new QRCode(UUID.randomUUID().toString());
        QRCode qr2 = new QRCode("hello");

        Post post = new Post("tmp", qr, playerId);
        postDB.add(post).addOnFailureListener(e -> fail(e.getLocalizedMessage()));
        post.setCode(qr2);
        postDB.add(post).addOnFailureListener(e -> fail(e.getLocalizedMessage()));
    }

//    Both tests test that there are top posts and there is a player with a specific Post
//    need to go more in depth with getPostByPlayerTest moving forward
    @Test
    public void getTopPostsTest() throws ExecutionException, InterruptedException {
        LiveData<List<Post>> posts = postDB.getTopPosts(1);
        assertNotNull(posts);
    }

    @Test
    public void getPostByPlayerTest() throws ExecutionException, InterruptedException {
        String playerId = addWithRandomHelper();
        LiveData<List<Post>> posts = postDB.getPostByPlayer(playerId);
        assertNotNull(posts);
    }

}

