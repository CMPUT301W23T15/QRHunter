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
 * Contains LiveData for player scores and CollectionReference for players and scores.
 * Allows for updating of player scores and retrieval of player and score data.
 */
public class RankinglistTotalViewModel extends ViewModel {
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
     * Sets up the playerCollection, playerScores, and initializes its value to an empty HashMap.
     */
    public RankinglistTotalViewModel() {
        mText = new MutableLiveData<>();
        PlayerRepository playerRepository = new PlayerRepository();
        playerCollection = playerRepository.getCollectionRef();
        playerScores = new MutableLiveData<>();
        playerScores.setValue(new HashMap<>());
    }

    /**
     * Getter for the playerScores LiveData.
     *
     * @return LiveData for the playerScores Hashmap
     */
    public LiveData<Map<String, Integer>> getPlayerScores() {
        return playerScores;
    }

    /**
     * Method for updating a player's score in the playerScores HashMap.
     *
     * @param playerName String representing the player's name.
     * @param score Integer representing the player's score.
     */
    public void updatePlayerScore(String playerName, int score) {
        Map<String, Integer> scores = playerScores.getValue();
        scores.put(playerName, score);
        playerScores.setValue(scores);
    }

    /**
     * Getter for the playerCollection CollectionReference.
     *
     * @return CollectionReference for the player collection
     */
    public CollectionReference getPlayerCollection() {
        return playerCollection;
    }

    /**
     * Getter for the scoresCollection CollectionReference.
     *
     * @return CollectionReference for the scores collection
     */
    public CollectionReference getScoresrCollection() {
        return scoresCollection;
    }

}

