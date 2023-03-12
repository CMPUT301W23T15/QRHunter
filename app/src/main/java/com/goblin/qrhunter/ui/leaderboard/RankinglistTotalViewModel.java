package com.goblin.qrhunter.ui.leaderboard;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.HashMap;
import java.util.Map;

public class RankinglistTotalViewModel extends ViewModel {
    private MutableLiveData<Map<String, Integer>> playerScores;

    public RankinglistTotalViewModel() {
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
}

