package com.goblin.qrhunter.ui.also;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.goblin.qrhunter.Score;
import com.goblin.qrhunter.data.PostRepository;

import java.util.List;

public class AlsoViewModel extends ViewModel {

    private PostRepository postDB;
    private LiveData<List<Score>> scores;

    public AlsoViewModel() {
        postDB = new PostRepository();
    }

    public LiveData<List<Score>> getByQR(String hash) {
        return postDB.getScoreByQR(hash);
    }

}
