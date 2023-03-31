package com.goblin.qrhunter.ui;

import android.app.Activity;

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

public class MapFragmentTest {
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

    /**
     * Needs more work !!!!
     */
    @Test
    public void MapOpenTest(){
        solo.assertCurrentActivity("wrong activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.map_button));
        solo.sleep(4000);
        solo.scrollToSide(Solo.RIGHT);

//        solo.sleep(1000);
//        for (int i =0; i<6; i++){
//            solo.clickOnView(solo.getView(R.id.map));
//        }
//        solo.scrollToSide(Solo.RIGHT);

        solo.sleep(2000);

//        click on marker?

        solo.goBack();
    }
}
