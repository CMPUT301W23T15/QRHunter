package com.goblin.qrhunter.ui;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.view.KeyEvent;
import android.widget.EditText;

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

public class MainActivityTest {
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

    @Test
    public void CheckProfile(){
        solo.assertCurrentActivity("Wrong activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.profile_button));
        solo.sleep(1000);
        solo.clickOnButton("Edit");
        solo.sleep(1000);
        solo.enterText((EditText) solo.getView(R.id.dialog_edit_phone_number), "780-888-9821");
        solo.clickOnView(solo.getView(R.id.button_save));
        solo.sleep(1000);
        solo.goBack();
        solo.clickOnView(solo.getView(R.id.profile_button));
        solo.sleep(1000);

        solo.clickOnButton("sign-out");
        solo.clickOnButton("get started");
        solo.assertCurrentActivity("Wrong activity", MainActivity.class);
        solo.sleep(1000);
    }

    @Test
    public void CheckLeaderBoards(){
        solo.assertCurrentActivity("Wrong activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.leaderboard_button));
        solo.sleep(1000);
        solo.clickOnButton("TOTAL SCORE");
        solo.sleep(2000);
        solo.goBack();
        solo.goBack();
    }

    @Test
//    the camera is a third party access so just make sure for now that when clicked it does in fact pull up camera
    public void CheckCamera(){
        solo.assertCurrentActivity("wrong activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.scan_button));
        solo.sleep(2000);
        solo.goBack();
        solo.goBack();
    }

    @Test
    public void CheckMap(){
        solo.assertCurrentActivity("wrong activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.map_button));
        solo.sleep(2000);
        solo.goBack();
    }

    @Test
    public void CheckNavigationSearch(){
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

    @Test
    public void CheckNavSummary(){
        solo.assertCurrentActivity("wrong activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.navigation_summary));
        solo.sleep(2000);
        solo.goBack();
    }

    /**
     * END 2 END is commented for now, for new features -> might move into new file because may conflict with what was done
     * Also not even sure if needed, but good to have for future
     * RUN SPECIFICALLY IF WANT -> AS RUNNING ALL MAY CAUSE CONFLICTS BETWEEN THE DATABASE OF PLAYERS ADDED
     */

    @After
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }

}
