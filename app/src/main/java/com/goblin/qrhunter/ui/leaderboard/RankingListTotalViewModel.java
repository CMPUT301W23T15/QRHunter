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

/**
 *
 */
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

    /**
     * Constructs a new RankingListTotalViewModel and initializes the LiveData
     */
    public RankingListTotalViewModel() {
        mText = new MutableLiveData<>();
        PlayerRepository playerRepository = new PlayerRepository();
        playerCollection = playerRepository.getCollectionRef();
        playerScores = new MutableLiveData<>();
        playerScores.setValue(new HashMap<>());
    }

    /**
     * Returns a LiveData object that holds the mapping from player username to score
     * @return LiveData Map of strings and integers
     */
    public LiveData<Map<String, Integer>> getPlayerScores() {
        return playerScores;
    }

    /**
     * Updates the player score with the passed in score
     * @param playerName specified player
     * @param score new score
     */
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

