package com.goblin.qrhunter.ui.map;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.goblin.qrhunter.Post;
import com.goblin.qrhunter.data.PostRepository;
import com.google.android.gms.location.FusedLocationProviderClient;

import java.util.List;

/**
 * This class is responsible for holding and processing data for the map screen.
 * It provides access to nearby posts to display on the map.
 */
public class MapViewModel extends ViewModel {

    private PostRepository postDB;
    private List<Post> nearby;
    private FusedLocationProviderClient fusedLocationClient;

    /**
     * Constructs a new MapViewModel and initializes the PostRepository.
     */
    public MapViewModel() {

        postDB = new PostRepository();

    }
    /**
     * Returns a LiveData object that holds a list of nearby posts.
     *
     * @param lat the latitude of the user's current location
     * @param lng the longitude of the user's current location
     * @param radius the radius in meters to search for posts around the user's location
     *
     * @return LiveData object that holds a list of nearby posts.
     */
    public LiveData<List<Post>> findNearby(double lat, double lng, double radius) {
        return postDB.getNearBy(lat, lng, radius);
    }


}