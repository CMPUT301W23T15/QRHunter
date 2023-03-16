package com.goblin.qrhunter.ui.leaderboard;

import android.os.Bundle;
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
        // Create a new instance of HomeViewModel
        RankinglistTotalViewModel rankinglistTotalViewModel = new ViewModelProvider(this).get(RankinglistTotalViewModel.class);


        View rootView = inflater.inflate(R.layout.fragment_rankbytotalscore, container, false);

        ListView listView = rootView.findViewById(R.id.listView_totalscore);


        SimpleAdapter adapter = new SimpleAdapter(
                requireContext(),
                getPlayerScoresList(),
                R.layout.player_ranking_content,
                new String[]{"name", "score"},
                new int[]{R.id.player_name, R.id.player_score});
        listView.setAdapter(adapter);


        return rootView;
    }


    private List<Map<String, String>> getPlayerScoresList() {
        List<Map<String, String>> list = new ArrayList<>();
        GetPlayersScoreUseCase scoreUseCase = new GetPlayersScoreUseCase();

        Map<String, Integer> playerScores = scoreUseCase.get().getValue();
        if (playerScores != null) {
            for (String playerId : playerScores.keySet()) {
                String playerName = scoreUseCase.getPlayerName(playerId);
                int playerScore = scoreUseCase.getPlayerScore(playerId);
                Map<String, String> map = new HashMap<>();
                map.put("name", playerName);
                map.put("score", Integer.toString(playerScore));
                list.add(map);
            }
        }
        return list;
    }


}
