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


//    Note: as of rn, getTopPosts hasn't been used yet

// Uncomment when implement !!!

//@RunWith(AndroidJUnit4.class)
public class PostRepoTest {
//    Post post;
//    PostRepository postDB;
//    FirebaseFirestore fireStore;
//    FirebaseAuth fireAuth;
//    FirebaseUser user;
//
//    LiveData<List<Post>> data;
//    Query q;
//
//    public void setUser(FirebaseUser user) {
//        this.user = user;
//    }
//
//    /**
//     * Setup firebase to use the emulated enviroment;
//     */
//    @Before
//    public void init() {
//        fireStore = FirebaseFirestore.getInstance();
//        fireAuth = FirebaseAuth.getInstance();
//
//        try {
//            // 10.0.2.2 is the special IP address to connect to the 'localhost' of
//            // the host computer from an Android emulator.
//            fireStore.useEmulator("10.0.2.2", 8080);
//            fireAuth.useEmulator("10.0.2.2", 9099);
//
//
//        } catch (IllegalStateException e) {
//            // pass
//        } catch (Exception e) {
//            fail("failed to setup firebase emulator is firebase emulator running");
//        }
//
//
//        FirebaseFirestoreSettings firestoreSettings = new FirebaseFirestoreSettings.Builder()
//                .setPersistenceEnabled(false)
//                .build();
//        fireStore.setFirestoreSettings(firestoreSettings);
//        postDB = new PostRepository();
//    }

//    TODO: Need to implement the this test
//    @Test
//    public void getTopPostTest(){
//
    }
