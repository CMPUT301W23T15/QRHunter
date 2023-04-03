package com.goblin.qrhunter.ui.also;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.goblin.qrhunter.Score;
import com.goblin.qrhunter.data.PostRepository;

import java.util.List;

/**
 * ViewModel class for the AlsoFragment that manages the logic related to retrieving scores
 * by QR code hash from the PostRepository.
 */
public class AlsoViewModel extends ViewModel {

    private PostRepository postDB;
    private LiveData<List<Score>> scores;

    /**
     * Constructor for the AlsoViewModel class that initializes the PostRepository instance.
     */
    public AlsoViewModel() {
        postDB = new PostRepository();
    }

    /**
     * Retrieves a LiveData list of scores associated with a QR code hash from the PostRepository.
     *
     * @param hash The hash of the QR code to retrieve scores for.
     * @return A LiveData list of scores associated with the given QR code hash.
     */
    public LiveData<List<Score>> getByQR(String hash) {
        return postDB.getScoreByQR(hash);
    }

}
