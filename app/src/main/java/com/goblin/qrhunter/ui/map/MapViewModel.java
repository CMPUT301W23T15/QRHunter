package com.goblin.qrhunter.ui.map;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.goblin.qrhunter.Post;
import com.goblin.qrhunter.data.PostRepository;

import java.util.ArrayList;
import java.util.List;

public class MapViewModel extends ViewModel {

    private PostRepository postDB;
    private List<Post> nearby;

    public MapViewModel() {
        postDB = new PostRepository();
    }

    public LiveData<List<Post>> findNearby(double lat, double lng, double radius) {
        return postDB.getNearBy(lat, lng, radius);
    }



}