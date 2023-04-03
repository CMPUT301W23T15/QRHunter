package com.goblin.qrhunter.ui;

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

public class SummaryFragmentTest {
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
    public void SummaryCheckTest(){
        solo.assertCurrentActivity("wrong activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.navigation_summary));
        solo.sleep(2000);
        solo.goBack();
    }

    @Test
    public void addDeleteCommentTest(){
        solo.assertCurrentActivity("wrong activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.navigation_summary));
        solo.clickOnText("QR ID:");
        solo.clickOnView(solo.getView(R.id.add_comment_view));
        solo.sleep(2000);
        solo.sendKey(KeyEvent.KEYCODE_N);
        solo.sendKey(KeyEvent.KEYCODE_I);
        solo.sendKey(KeyEvent.KEYCODE_C);
        solo.sendKey(KeyEvent.KEYCODE_E);
        solo.sendKey(KeyEvent.KEYCODE_ESCAPE);
        solo.sleep(2000);
        solo.clickOnView(solo.getView(R.id.add_comment_btn));
        solo.sleep(1000);
        solo.clickLongOnText("nice");
        solo.sleep(1000);
        solo.clickOnMenuItem("Delete");
        solo.sleep(1000);
        solo.goBack();
    }


}
