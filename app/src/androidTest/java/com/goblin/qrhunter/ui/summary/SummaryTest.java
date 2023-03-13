package com.goblin.qrhunter.ui.summary;

import static org.junit.Assert.assertEquals;

import android.os.Looper;
import android.os.Handler;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import com.goblin.qrhunter.Post;
import com.goblin.qrhunter.ui.summary.SummaryViewModel;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;


public class SummaryTest {

    private SummaryViewModel sViewModel;
//    rq80dBJt01Syh42bGB1EH2Z3ViD3

    @Before
    public void setup() {
        sViewModel = new SummaryViewModel();
    }

    @Test
    public void testGetUsername(){
        String testMsg = "test";
        LiveData<String> ld = sViewModel.getUsername();
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onChanged(String s) {
                assertEquals(testMsg, s);
            }
        };
        new Handler(Looper.getMainLooper()).post(() -> {
            ld.observeForever(observer);
            sViewModel.setUsername(testMsg);
            ld.removeObserver(observer);
        });
    }

    @Test
    public void testGetUserPosts() {
        Post p = new Post();
        List<Post> lp = new ArrayList<>();
        lp.add(p);

        MediatorLiveData<List<Post>> ld = sViewModel.getUserPosts();
        Observer<List<Post>> observer = new Observer<List<Post>>() {
            @Override
            public void onChanged(List<Post> posts) {
                assertEquals(posts.get(0), p);
            }
        };
        new Handler(Looper.getMainLooper()).post(() -> {
            ld.observeForever(observer);
            sViewModel.setUserPosts(lp);
            ld.removeObserver(observer);
        });
    }

    @Test
    public void test() {

    }
}
