package com.goblin.qrhunter.ui;

import static org.junit.Assert.assertNull;
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

public class ProfileFragmentTest{
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
    public void ProfileLogIOEditTest(){
        solo.assertCurrentActivity("Wrong activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.profile_button));
        solo.sleep(1000);
        solo.clickOnButton("Edit");
        solo.sleep(1000);
        solo.enterText((EditText) solo.getView(R.id.dialog_edit_phone_number), "780-888-9821");
        solo.clickOnView(solo.getView(R.id.dialog_edit_email));
        for (int i = 0; i<4; i++){
            solo.sendKey(KeyEvent.KEYCODE_DPAD_RIGHT);
        }
        for (int i =0; i<18;i++){
            solo.sendKey(KeyEvent.KEYCODE_DEL);
        }
        solo.enterText((EditText) solo.getView(R.id.dialog_edit_email), "Proper@gmail.com");

        solo.clickOnView(solo.getView(R.id.button_save));
        solo.sleep(2000);
        solo.goBack();
        solo.clickOnView(solo.getView(R.id.profile_button));
        solo.sleep(1000);

        solo.clickOnButton("sign-out");
        solo.clickOnButton("get started");
        solo.assertCurrentActivity("Wrong activity", MainActivity.class);
        solo.sleep(1000);
    }

    /**
     * Test to ensure if user will enter the proper format our app wants
     */
    @Test
    public void FaultyEditProfilePhoneTest(){
//        too short
        solo.assertCurrentActivity("Wrong activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.profile_button));
        solo.sleep(1000);
        solo.clickOnButton("Edit");
        solo.enterText((EditText) solo.getView(R.id.dialog_edit_phone_number), "780");
        solo.sleep(1000);
        solo.clickOnView(solo.getView(R.id.button_save));
        solo.sleep(2000);
        assertTrue(solo.waitForText("Phone number must be between 7 and 15 characters"));
        solo.sleep(1000);
        solo.goBack();


        solo.clickOnButton("Edit");
        solo.enterText((EditText) solo.getView(R.id.dialog_edit_phone_number), "1234567891234567");
        solo.sleep(1000);
        solo.clickOnView(solo.getView(R.id.button_save));
        solo.sleep(2000);
        assertTrue(solo.waitForText("Phone number must be between 7 and 15 characters"));
        solo.goBack();
        solo.goBack();
    }
    @Test
    public void FaultyEditProfileEmailTest() {
//        too short
        solo.assertCurrentActivity("Wrong activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.profile_button));
        solo.sleep(1000);
        solo.clickOnButton("Edit");
        solo.clickOnView(solo.getView(R.id.dialog_edit_email));

        for (int i = 0; i<4; i++){
            solo.sendKey(KeyEvent.KEYCODE_DPAD_RIGHT);
        }
        for (int i =0; i<18;i++){
            solo.sendKey(KeyEvent.KEYCODE_DEL);
        }

        solo.enterText((EditText) solo.getView(R.id.dialog_edit_email), "SomethingNotRight");
        solo.sleep(1000);
        solo.enterText((EditText) solo.getView(R.id.dialog_edit_phone_number), "780-888-9821");
        solo.clickOnView(solo.getView(R.id.button_save));
        solo.sleep(2000);
        assertTrue(solo.waitForText("Invalid email format"));
        solo.sleep(1000);
        solo.goBack();
    }

}
