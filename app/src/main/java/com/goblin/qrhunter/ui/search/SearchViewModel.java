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
 * SearchViewModel stub
 */
public class SearchViewModel extends ViewModel {

    private final MutableLiveData<String> mText;
    private CollectionReference playerCollection;


    /**
     * Stub constructor
     */
    public SearchViewModel() {
        mText = new MutableLiveData<>();
        //mText.setValue("This is search fragment");
        PlayerRepository playerRepository = new PlayerRepository();
        playerCollection = playerRepository.getCollectionRef();

    }

    /**
     * SearchViewModel stub method for usage example
     */
    public LiveData<String> getText() {
        return mText;
    }

    public CollectionReference getPlayerCollection() {
        return playerCollection;
    }
}