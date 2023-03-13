package com.goblin.qrhunter.ui;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.SearchView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.goblin.qrhunter.MainActivity;
import com.goblin.qrhunter.R;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class MainActivityTest {
    private Solo solo;
    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class, true, true);

    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }
    // Gets the (main) activity
    public void start() throws Exception{
        Activity activity = rule.getActivity();
    }

    @Test
//    checks to see when clicked on profile to sign out and sign back in
    public void CheckProfileLogInLogOut(){
        solo.assertCurrentActivity("Wrong activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.profile_button));
        solo.clickOnButton("sign-out");
        solo.clickOnButton("get started");
        solo.sleep(1000);
        solo.assertCurrentActivity("Wrong activity", MainActivity.class);
        assertTrue(solo.waitForText("sign-out"));
        solo.goBack();
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
    }

    @Test
    public void CheckMap(){
        solo.assertCurrentActivity("wrong activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.map_button));
        solo.sleep(2000);
        solo.goBack();
    }

    @Test
    public void checkNavigationSearch(){
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
        assertTrue(solo.waitForText("user49394473"));
        solo.sleep(2000);
        solo.goBack();
    }

    @Test
    public void checkNavSummary(){
        solo.assertCurrentActivity("wrong activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.navigation_summary));
        assertTrue(solo.waitForText("My QR Codes"));
        solo.sleep(2000);
        solo.goBack();
    }

    @Test
    public void EndToEndFullTest(){
        CheckProfileLogInLogOut();
        CheckLeaderBoards();
        CheckCamera();
        CheckMap();
        checkNavigationSearch();
        checkNavSummary();
    }
    // Method 3) This method closes the activity after each test
    @After
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }

}
