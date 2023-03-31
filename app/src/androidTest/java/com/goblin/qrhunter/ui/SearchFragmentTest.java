package com.goblin.qrhunter.ui;

import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.view.KeyEvent;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.goblin.qrhunter.MainActivity;
import com.goblin.qrhunter.Player;
import com.goblin.qrhunter.R;
import com.goblin.qrhunter.data.PlayerRepository;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;


public class SearchFragmentTest {
    private Solo solo;
    private String username_test_value = "user49394473";
    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class, true, true);

    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
        PlayerRepository playerRepository = new PlayerRepository();
        Task<Player> getPlayer = playerRepository.getPlayerByUsername(username_test_value);
        Tasks.await(getPlayer);
        if(getPlayer.getResult() == null) {
            Player p1 = new Player();
            p1.setUsername(username_test_value);
            playerRepository.add(p1);
        }
    }
    // Gets the (main) activity
    public void start() throws Exception{
        Activity activity = rule.getActivity();
    }

    @After
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }

    @Test
    public void SearchFaultyTest(){
        solo.assertCurrentActivity("wrong activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.navigation_search));
        assertTrue(solo.waitForText("Search for other players"));
        solo.clickOnView(solo.getView(R.id.player_search));
        solo.sendKey(KeyEvent.KEYCODE_N);
        solo.sendKey(KeyEvent.KEYCODE_O);
        solo.sendKey(KeyEvent.KEYCODE_N);
        solo.sendKey(KeyEvent.KEYCODE_E);
        solo.sendKey(KeyEvent.KEYCODE_ENTER);
        solo.sleep(1000);
    }


    @Test
    public void SearchSuccessTest() {
        solo.assertCurrentActivity("wrong activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.navigation_search));
        assertTrue(solo.waitForText("Search for other players"));
        solo.clickOnView(solo.getView(R.id.player_search));
        solo.sendKey(KeyEvent.KEYCODE_U);
        solo.sendKey(KeyEvent.KEYCODE_S);
        solo.sendKey(KeyEvent.KEYCODE_E);
        solo.sendKey(KeyEvent.KEYCODE_R);
        solo.sendKey(KeyEvent.KEYCODE_4);
        solo.sendKey(KeyEvent.KEYCODE_9);
        solo.sendKey(KeyEvent.KEYCODE_3);
        solo.sendKey(KeyEvent.KEYCODE_9);
        solo.sendKey(KeyEvent.KEYCODE_ENTER);
        assertTrue(solo.waitForText(username_test_value));
        solo.sleep(2000);
        solo.clickOnView(solo.getView(R.id.searched_username));
        solo.sleep(1000);
        solo.goBack();
        solo.goBack();
    }
}
