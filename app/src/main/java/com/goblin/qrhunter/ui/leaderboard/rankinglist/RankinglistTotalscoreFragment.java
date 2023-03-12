package com.goblin.qrhunter.ui.leaderboard.rankinglist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.goblin.qrhunter.R;
import com.goblin.qrhunter.domain.GetPlayersScoreUseCase;
import com.goblin.qrhunter.ui.leaderboard.LeaderboardFragment;
import com.goblin.qrhunter.ui.leaderboard.LeaderboardViewModel;

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

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_rankbytotalscore, container, false);

        ListView listView = rootView.findViewById(R.id.listView_totalscore);
        SimpleAdapter adapter = new SimpleAdapter(
                requireContext(),
                getPlayerScoresList(),
                R.layout.list_player_item,
                new String[]{"name", "score"},
                new int[]{R.id.player_name, R.id.player_score});
        listView.setAdapter(adapter);

        return rootView;
    }

    private List<Map<String, String>> getPlayerScoresList() {
        List<Map<String, String>> list = new ArrayList<>();
        GetPlayersScoreUseCase scoreUseCase = new GetPlayersScoreUseCase();

        for (String playerId : scoreUseCase.get().getValue().keySet()) {
            String playerName = scoreUseCase.getPlayerName(playerId);
            int playerScore = scoreUseCase.getPlayerScore(playerId);
            Map<String, String> map = new HashMap<>();
            map.put("name", playerName);
            map.put("score", Integer.toString(playerScore));
            list.add(map);
        }
        return list;
    }

}
