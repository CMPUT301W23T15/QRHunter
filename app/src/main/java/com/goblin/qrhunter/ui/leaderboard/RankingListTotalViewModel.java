package com.goblin.qrhunter.ui.leaderboard;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.goblin.qrhunter.Post;
import com.goblin.qrhunter.data.PlayerRepository;
import com.goblin.qrhunter.data.PostRepository;
import com.google.firebase.firestore.CollectionReference;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RankingListTotalViewModel extends ViewModel {
    private final MutableLiveData<String> mText;
    private CollectionReference playerCollection;
    private CollectionReference scoresCollection;
    MutableLiveData<String> username = new MutableLiveData<>();
    LiveData<List<Post>> userPosts;

    String playerId;

    PostRepository postDB;
    PlayerRepository playerDB;

    private MutableLiveData<Map<String, Integer>> playerScores;

    public RankingListTotalViewModel() {
        mText = new MutableLiveData<>();
        PlayerRepository playerRepository = new PlayerRepository();
        playerCollection = playerRepository.getCollectionRef();
        playerScores = new MutableLiveData<>();
        playerScores.setValue(new HashMap<>());
    }

    public LiveData<Map<String, Integer>> getPlayerScores() {
        return playerScores;
    }

    public void updatePlayerScore(String playerName, int score) {
        Map<String, Integer> scores = playerScores.getValue();
        scores.put(playerName, score);
        playerScores.setValue(scores);
    }
    public CollectionReference getPlayerCollection() {
        return playerCollection;
    }
    public CollectionReference getScoresrCollection() {
        return scoresCollection;
    }

}

