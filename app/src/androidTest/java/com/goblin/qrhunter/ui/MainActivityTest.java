package com.goblin.qrhunter.ui;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.app.Activity;

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
    }

    @Test
    public void CheckCamera(){
        solo.assertCurrentActivity("wrong activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.scan_button));
    }

    @Test
    public void checkNavigationSearch(){
        solo.assertCurrentActivity("wrong activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.navigation_search));
        assertTrue(solo.waitForText("This is search fragment"));
        solo.sleep(3000);
    }

    @Test
    public void checkNav(){
        solo.assertCurrentActivity("wrong activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.navigation_summary));
        assertTrue(solo.waitForText("My QR Codes"));
        solo.sleep(3000);
    }










    // Method 3) This method closes the activity after each test
    @After
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }

}
