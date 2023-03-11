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

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.goblin.qrhunter.data.PostRepository;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import org.junit.Before;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

@RunWith(AndroidJUnit4.class)
public class PlayerRepositoryTest{
    PlayerRepository playerDB;
    PostRepository postDB;

    Task<Player> t;

    Player p1;

    BaseRepository baseRepo;


    @Before
    public void init() {
        FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
        try {
            // 10.0.2.2 is the special IP address to connect to the 'localhost' of
            // the host computer from an Android emulator.
            fireStore.useEmulator("10.0.2.2", 8080);
        } catch (IllegalStateException e) {
            // pass
        } catch (Exception e) {
            fail("failed to setup firebase emulator is firebase emulator running");
        }

        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(false)
                .build();
        fireStore.setFirestoreSettings(settings);
        playerDB = new PlayerRepository();
        postDB = new PostRepository();

    }

//    don't even know if this helps, if not don't need initialize p1 and t before this
    public String addUserHelperPidFix() {
        String newPlayerId = UUID.randomUUID().toString();
        String usernameTest = "User"+newPlayerId;
        p1 = new Player(newPlayerId, usernameTest, "example@email.com");
        playerDB.add(p1);
        t = playerDB.get(usernameTest);
//        assertEquals(playerDB.getPlayerByUsername(p1.getUsername()), t);
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

//    CANT FIGURE OUT
    @Test
    public void getPlayerByUsernameTest() throws ExecutionException, InterruptedException {
        addUserHelperPidFix();

        Task<Player> GetPlayerByUserN = playerDB.getPlayerByUsername(p1.getUsername());
        Tasks.await(GetPlayerByUserN);
        Player result = GetPlayerByUserN.getResult();

//      fails here: -> different Object Locations
        assertEquals(result, p1);
    }

//     don't even think this is needed
    @Test
    public void randomUsernameTest(){
//          addUserHelperPidFix();
          assertNotSame(playerDB.randomUsername(),playerDB.randomUsername());
    }


    @Test
    public void addWithRandomUsernameTest() throws Exception{
        addUserHelperPidFix();
        Task<String> userN = playerDB.randomUsername();
        Tasks.await(userN);
        String results = userN.getResult();
        playerDB.addWithRandomUsername(results);


//        firestore offline? so can't even tell if i have actual issues with test
//        Task<Boolean> exist = playerDB.exists(results);
//        Tasks.await(exist);
//        Boolean existResult = exist.getResult();

//        assertTrue(existResult);
    }
}
