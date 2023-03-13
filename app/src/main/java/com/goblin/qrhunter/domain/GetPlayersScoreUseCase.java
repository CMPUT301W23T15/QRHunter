/**
 * This class defines the use case for obtaining the scores of all players
 * in the app.
 */
package com.goblin.qrhunter.domain;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.goblin.qrhunter.Player;
import com.goblin.qrhunter.Post;
import com.goblin.qrhunter.data.PlayerRepository;
import com.goblin.qrhunter.data.PostRepository;
import com.google.android.gms.tasks.Task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * implementation of the GetPlayersScoreUseCase class,
 *  responsible for calculating the scores of all players based on their
 * posts and returning a MediatorLiveData object that can be observed by UI components.
 */
public class GetPlayersScoreUseCase {
    private PostRepository postDB;
    private LiveData<List<Post>> allPosts;

    private MediatorLiveData<Map<String, Integer>> playerScores = new MediatorLiveData<>();

    PlayerRepository playerRepository = new PlayerRepository();


    /**
     * Constructor for the use case. Sets up repo's and livedata.
     */
    public GetPlayersScoreUseCase() {
        postDB = new PostRepository();
        allPosts = postDB.getAll();
        playerScores.addSource(allPosts, this::refresh);
    }


    /**
     * Refreshes the player scores based on the latest post data.
     *
     * @param postList the list of posts to refresh from
     */
    private void refresh(List<Post> postList) {
        HashMap<String, Integer> scoresMap = new HashMap<>();
        for (Post post : postList) {
            // null guards
            if (post == null || post.getCode() == null || post.getPlayerId() == null) {
                continue;
            }

            // if user does exit in map create user
            if (!scoresMap.containsKey(post.getPlayerId())) {
                scoresMap.put(post.getPlayerId(), 0);
            }
            // add post score to the sum for that player.
            int newScore = scoresMap.get(post.getPlayerId()) + post.getCode().getScore();
            scoresMap.put(post.getPlayerId(), newScore);
        }
        playerScores.setValue(scoresMap);
    }

    /**
     * Gets the player scores as a live data object.
     *
     * @return the live data object containing the player scores
     */
    public MediatorLiveData<Map<String, Integer>> get() {
        return playerScores;
    }

    public MediatorLiveData<Integer> getById(@NonNull String playerId) {
        return new MediatorLiveData<>() {{
            addSource(playerScores, stringIntegerMap -> {
                int score = 0;
                if(stringIntegerMap != null && stringIntegerMap.get(playerId) != null) {
                    score = stringIntegerMap.get(playerId);
                }
                this.setValue(score);
            });
        }};
    }

    /**
     * Get the player total score based on player ID
     * @param playerId
     * @return
     */
    public int getPlayerScore(String playerId) {
        Map<String, Integer> scoresMap = playerScores.getValue();
        if (scoresMap != null && scoresMap.containsKey(playerId)) {
            return scoresMap.get(playerId);
        } else {
            return 0;
        }
    }

    /**
     * Get the player user name based on player ID
     * @param playerId
     * @return
     */
    public String getPlayerName(String playerId) {
        Map<String, Integer> scoresMap = playerScores.getValue();
        if (scoresMap != null && scoresMap.containsKey(playerId)) {
            // Get the player's name from the PlayerRepository based on the player ID
            Task<Player> player = playerRepository.getPlayerById(playerId);

            if (player != null) {
                return player.getResult().getUsername();
            }
        }
        return null;
    }


}
