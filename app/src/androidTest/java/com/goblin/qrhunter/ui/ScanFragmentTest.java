package com.goblin.qrhunter.ui;

import android.app.Activity;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.goblin.qrhunter.MainActivity;
import com.goblin.qrhunter.Player;
import com.goblin.qrhunter.R;
import com.goblin.qrhunter.data.PlayerRepository;
import com.goblin.qrhunter.ui.scan.ScanActivity;
import com.goblin.qrhunter.ui.takephoto.TakePhotoActivity;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class ScanFragmentTest {
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
    public void ScannerOpenTest(){
        solo.assertCurrentActivity("wrong activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.scan_button));
        solo.assertCurrentActivity("Wrong", ScanActivity.class);
        solo.sleep(2000);
        solo.goBack();
    }

    /**
     * Scanner flow test to scan a qr code and take picture
     * You will have 10 seconds to scan a qr code, or app will exit
     * and pass either way showing a toast for nothing scanned.
     * TODO: find a way for camera to take picture, and proceed with the flow
     */
    @Test
    public void ScannerFlowNoCameraTest(){
        solo.assertCurrentActivity("wrong activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.scan_button));
        solo.assertCurrentActivity("Wrong", ScanActivity.class);
//        10 seconds to scan a qr code for test
        solo.sleep(1000*10);
        if (solo.waitForText("Your QR code is worth:")){
            solo.clickOnButton("OK");
            solo.sleep(2000);
            solo.waitForText("Do you want to take a photo");
            solo.clickOnButton("No");
            solo.sleep(1000);
            solo.clickOnView(solo.getView(R.id.button_location));
            solo.sleep(1000);
            solo.clickOnButton("Yes");
            solo.sleep(2000);
            solo.clickOnView(solo.getView(R.id.button_add));
            solo.sleep(2000);

//            solo.clickOnView(solo.getView(R.id.camera_button));
//            solo.sleep(2000);
//            solo.assertCurrentActivity("should be in photo", TakePhotoActivity.class);
//            find way to click on take photo!!!!!

        }
        else solo.goBack();
    }

    @Test
    public void CheckSummaryPostAddedTest(){
        solo.assertCurrentActivity("wrong activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.navigation_summary));
        solo.sleep(1000);
//        idk what this view is, click on it and then go back
    }



}
