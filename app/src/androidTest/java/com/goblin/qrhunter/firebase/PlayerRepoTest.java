package com.goblin.qrhunter.firebase;

import com.goblin.qrhunter.Player;
import com.goblin.qrhunter.data.PlayerRepository;
import com.goblin.qrhunter.data.BaseRepository;

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

import java.util.UUID;
import java.util.concurrent.ExecutionException;


/**
 * With much help of Zachary
 * To run this test: terminal -> firebase emulator:start -> run unit tests
 */
@RunWith(AndroidJUnit4.class)
public class PlayerRepoTest {
    PlayerRepository playerDB;
    PostRepository postDB;
    FirebaseFirestore fireStore;
    FirebaseAuth fireAuth;

    FirebaseUser user;

    public void setUser(FirebaseUser user) {
        this.user = user;
    }

    /**
     * Setup firebase to use the emulated enviroment;
     */
    @Before
    public void init() {
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
        playerDB = new PlayerRepository();

    }


    // TODO: decide if its a good idea to move generic tests to different file

    /**
     * create a new player, for methods that require updating
     *
     * @return the new player
     */
    public Player baseAddHelper() throws ExecutionException, InterruptedException{
        String newPlayerId = UUID.randomUUID().toString();
        Player p1 = new Player(newPlayerId, "User" + newPlayerId, "example@email.com");

        // create a new task to add a player to the database
        Task<Void> addPlayerTask = playerDB.add(p1);
        Tasks.await(addPlayerTask);
        // create a new task to get a player from the database
        Task<Player> getPlayerTask = playerDB.get(newPlayerId);
        Tasks.await(getPlayerTask);
        // await tasks to complete
        assertTrue((p1.getUsername().equals(getPlayerTask.getResult().getUsername())));
        assertTrue((p1.getId().equals(getPlayerTask.getResult().getId())));
        return p1;
    }

    @Test
    public void baseAddTest() throws ExecutionException, InterruptedException {
        Player p1 = baseAddHelper();
        if (p1 == null) {
            fail("base repository method add failed");
        }
    }


    /**
     *  create a new player with a random user name
     * @return playerId of newly created user
     * @throws ExecutionException
     * @throws InterruptedException
     */
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


    /**
     * addWithRandomTest runs addWithRandomHelper if any exception are raise task will fail.
     */
    @Test
    public void addWithRandomTest() throws ExecutionException, InterruptedException {
        String playerId = addWithRandomHelper();
        if(playerId == null || playerId.isEmpty()) {
            fail("unknown failure");
        }
    }

    @Test
    public void getPlayerByUsernameTest() throws ExecutionException, InterruptedException {
        // create a random player
        String playerId = addWithRandomHelper();

        // task to retrieve the player we just created by id.
        Task<Player> playerByIdTask = playerDB.get(playerId);
        Tasks.await(playerByIdTask);
        Player playerById = playerByIdTask.getResult();

        assertNotNull(playerById);
        assertNotNull(playerById.getUsername());

        // task to get player by username
        String username = playerById.getUsername();
        Task<Player> PlayerByUsernameTask = playerDB.getPlayerByUsername(username);
        playerByIdTask.continueWith((Continuation<Player, Object>) task -> {
            // continueWith means task has completed
            Player playerByUsername = task.getResult();
            // check that the player we got by searching username is not null
            assertNotNull(playerByUsername);

            assertEquals(playerById,playerByUsername);
            return null;
        });
    }

//    @Test
////    possibly don't even need this test: After the testing passes, build will crash.
//    public void addWithRandomUserNameTest() throws ExecutionException, InterruptedException {
//        // create a random player
//        String playerId = addWithRandomHelper();
//
//        // task to retrieve the player we just created by id.
//        Task<Player> playerByIdTask = playerDB.get(playerId);
//        Tasks.await(playerByIdTask);
//        Player playerById = playerByIdTask.getResult();
//
//        assertNotNull(playerById);
//        assertNotNull(playerById.getUsername());
//
////        since we don't need to retrieve anything, and its a random username,
////        we make sure the task is successful
//        playerByIdTask.continueWith((Continuation<Player, Object>) task -> {
//            if(!playerDB.addWithRandomUsername(playerId).isSuccessful()){
//                fail("did not add");
//            }
//            return null;
//        });
//    }
}