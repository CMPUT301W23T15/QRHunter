/**
 * SearchViewModel stub
 */
package com.goblin.qrhunter.ui.search;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.goblin.qrhunter.data.PlayerRepository;
import com.google.firebase.firestore.CollectionReference;

/**
 * The SearchViewModel class is responsible for managing
 * and providing data related to the search screen of the application.
 */
public class SearchViewModel extends ViewModel {

    private final MutableLiveData<String> mText;
    private CollectionReference playerCollection;


    /**
     * Constructs a new instance of SearchViewModel and gets a reference to the Firebase players
     * collection
     */
    public SearchViewModel() {
        mText = new MutableLiveData<>();
        //mText.setValue("This is search fragment");
        PlayerRepository playerRepository = new PlayerRepository();
        playerCollection = playerRepository.getCollectionRef();

    }

    public LiveData<String> getText() {
        return mText;
    }

    public CollectionReference getPlayerCollection() {
        return playerCollection;
    }
}