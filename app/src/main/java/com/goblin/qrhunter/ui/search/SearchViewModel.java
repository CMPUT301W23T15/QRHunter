/**
 * SearchViewModel stub
 */
package com.goblin.qrhunter.ui.search;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * SearchViewModel stub
 */
public class SearchViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    /**
     * Stub constructor
     */
    public SearchViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is search fragment");

    }

    /**
     * SearchViewModel stub method for usage example
     */
    public LiveData<String> getText() {
        return mText;
    }
}