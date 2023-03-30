package com.goblin.qrhunter.ui.leaderboard;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.goblin.qrhunter.R;
import com.goblin.qrhunter.domain.GetPlayersScoreUseCase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Displays a ranking list of players with their total scores.
 */

public class RankinglistTotalscoreFragment extends Fragment {
    private RankinglistTotalViewModel mViewModel;

    /**
     * Creates a new instance of the RankinglistTotalscreFragment.
     *
     * @return a new instance of RankinglistTotalscreFragment.
     */
    public static RankinglistTotalscoreFragment newInstance() {
        return new RankinglistTotalscoreFragment();
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Create a new instance of HomeViewModel
        RankinglistTotalViewModel rankinglistTotalViewModel = new ViewModelProvider(this).get(RankinglistTotalViewModel.class);
        View rootView = inflater.inflate(R.layout.fragment_rankbytotalscore, container, false);
        Pair<List<Map<String, String>>, ListView> playerScoresPair = getPlayerScoresList();
        List<Map<String, String>> playerScoresList = playerScoresPair.first;
        ListView listView = playerScoresPair.second;
        SimpleAdapter adapter = new SimpleAdapter(
                requireContext(),
                playerScoresList,
                R.layout.player_ranking_content,
                new String[]{"name", "score"},
                new int[]{R.id.player_name, R.id.player_score});
        listView.setAdapter(adapter);

        db.collection("scores")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String playerId = document.getId();
                            String playerName = document.getString("name");
                            Long score = document.getLong("score");
                            int playerScore = score != null ? score.intValue() : 0;

                        }
                    } else {
                    Log.d(TAG, "RANK Error getting documents: ", task.getException());
                    }
                });

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RankinglistTotalViewModel rankinglistTotalViewModel = new ViewModelProvider(this).get(RankinglistTotalViewModel.class);
    }



    private Pair<List<Map<String, String>>, ListView> getPlayerScoresList() {
        List<Map<String, String>> list = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference playersRef = db.collection("players");
        Query query = playersRef.orderBy("totalScore", Query.Direction.DESCENDING);

        ListView listView = new ListView(requireContext());

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Map<String, Object> data = document.getData();
                    String playerName = (String) data.get("name");
                    Long playerScore = (Long) data.get("totalScore");

                    Map<String, String> map = new HashMap<>();
                    map.put("name", playerName);
                    map.put("score", playerScore.toString());
                    list.add(map);
                }
                // update the list view with the retrieved data
                SimpleAdapter adapter = new SimpleAdapter(
                        requireContext(),
                        list,
                        R.layout.player_ranking_content,
                        new String[]{"name", "score"},
                        new int[]{R.id.player_name, R.id.player_score});

                listView.setAdapter(adapter);
            } else {
                Log.d(TAG, "Error getting documents: ", task.getException());
            }
        });

        return new Pair<>(list, listView);
    }

//        GetPlayersScoreUseCase scoreUseCase = new GetPlayersScoreUseCase();
//
//        Map<String, Integer> playerScores = scoreUseCase.get().getValue();
//        if (playerScores != null) {
//            for (String playerId : playerScores.keySet()) {
//                String playerName = scoreUseCase.getPlayerName(playerId);
//                int playerScore = scoreUseCase.getPlayerScore(playerId);
//                Map<String, String> map = new HashMap<>();
//                map.put("name", playerName);
//                map.put("score", Integer.toString(playerScore));
//                list.add(map);
//            }
//        }

    }



