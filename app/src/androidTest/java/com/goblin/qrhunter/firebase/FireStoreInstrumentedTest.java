package com.goblin.qrhunter.firebase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.goblin.qrhunter.Player;
import com.goblin.qrhunter.Post;
import com.goblin.qrhunter.QRCode;
import com.goblin.qrhunter.data.PlayerRepository;
import com.goblin.qrhunter.data.PostRepository;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.UUID;

/**
 * Firebase Instrumented test, which will execute on an Android device.
 * requires local firebase emulator running
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class FireStoreInstrumentedTest {
    PlayerRepository playerDB;
    PostRepository postDB;

    @Before
    public void init() {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        try {
            // 10.0.2.2 is the special IP address to connect to the 'localhost' of
            // the host computer from an Android emulator.
            firestore.useEmulator("10.0.2.2", 8080);
        } catch (IllegalStateException e) {
            // pass
        } catch (Exception e) {
            fail("failed to setup firebase emulator is firebase emulator running");
        }

        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(false)
                .build();
        firestore.setFirestoreSettings(settings);
        playerDB = new PlayerRepository();
        postDB = new PostRepository();
    }

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.goblin.qrhunter", appContext.getPackageName());
    }

    public String addUserHelper() {
        String newPlayerId = UUID.randomUUID().toString();
        Player p1 = new Player(newPlayerId, "User"+newPlayerId, "example@email.com");
        playerDB.add(p1);
        Task<Player> t = playerDB.get(newPlayerId);
        try {
            Tasks.await(t);
            if(t.getResult() == null) {
                return null;
            }
            return t.getResult().getId();
        } catch (Exception e) {
            return null;
        }
    }
    @Test
    public void addUser() {
        String userId = addUserHelper();
        assertNotNull(userId);
        assertFalse(userId.isEmpty());
    }



    @Test
    public void deleteUser() {
        String pId = addUserHelper();
        playerDB.delete(pId);
        playerDB.exists(pId).addOnCompleteListener(task -> {
            assertFalse(task.getResult());
        });
    }

    @Test
    public void addPost() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Post p1 = new Post("post1", new QRCode("hello world"), user.getUid());
        postDB.add(p1).continueWith(task -> {
            if(!task.isSuccessful()) {
                fail("could not add post");
            }
            return null;
        });
    }
}

