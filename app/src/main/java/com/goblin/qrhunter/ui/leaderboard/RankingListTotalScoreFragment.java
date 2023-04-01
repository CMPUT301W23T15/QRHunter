package com.goblin.qrhunter.ui.leaderboard;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.goblin.qrhunter.Player;
import com.goblin.qrhunter.R;
import com.goblin.qrhunter.databinding.FragmentRankbytotalscoreBinding;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;


public class RankingListTotalScoreFragment extends Fragment {

   // private GetPlayersScoreUseCase getPlayersScoreUseCase;
   private static final String TAG = "RankinglistTotalFrag";

    private ListView ranklist_view;
    private RankingListTotalViewModel viewModel;
    NavController navController;
    private FragmentRankbytotalscoreBinding binding;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(RankingListTotalViewModel.class);
        navController = Navigation.findNavController(container);


        // Fragment Set Up
        binding = FragmentRankbytotalscoreBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        Log.d(TAG, "scores");

        // Set up list view
        ranklist_view = binding.getRoot().findViewById(R.id.listView_totalscore);


        //  Create a list of players
        List<Player> players = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference scoresRef = db.collection("scores");

        // Retrieves the player scores ordered by the total score in descending order
        // and updates the player scores in the ViewModel
        scoresRef.orderBy("totalScore", Query.Direction.DESCENDING).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        String playerName = document.getString("player.username");
//                        Log.d("Firebase", "Player name: " + playerName);
                        int totalScore = document.getLong("totalScore").intValue();

                        //if the user has no QR code

                        Player player = new Player(playerName, totalScore);
                        players.add(player);
                    }

                    // Once all the data is fetched, you can update the player scores in your ViewModel
                    for (Player player : players) {
                        viewModel.updatePlayerScore(player.getUsername(), player.getTotalScore());
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error fetching player scores", e);
                });
        //Set the rank for players
        int rank = 1;
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            if (i > 0 && player.getTotalScore() < players.get(i - 1).getTotalScore()) {
                rank++;
            }
            player.setRank(rank);
        }

        //Update the UI with the sorted list of players including their rank
        Collections.sort(players, new Comparator<Player>() {
            @Override
            public int compare(Player p1, Player p2) {
                return Integer.compare(p2.getTotalScore(), p1.getTotalScore());
            }
        });
        for (int i = 0; i < players.size(); i++) {
            players.get(i).setRank(i + 1);
        }

        PlayerAdapter adapter = new PlayerAdapter(requireContext(), players);
        ranklist_view.setAdapter(adapter);
        adapter.notifyDataSetChanged();



        // if scores are updated in ViewModel
        // re-generate the list of players and update the ListView adapter
        viewModel.getPlayerScores().observe(getViewLifecycleOwner(), new Observer<Map<String, Integer>>() {
            @Override
            public void onChanged(Map<String, Integer> playerScores) {
                List<Player> playerList = new ArrayList<>();
                int rank = 1;
                for (String player : playerScores.keySet()) {
                    int score = playerScores.get(player);
                    Player playerObject = new Player(player, score);
                    playerList.add(playerObject);
                }
                Collections.sort(playerList, new Comparator<Player>() {
                    @Override
                    public int compare(Player p1, Player p2) {
                        return Integer.compare(p2.getTotalScore(), p1.getTotalScore()); // Sort in descending order
                    }
                });
                for (int i = 0; i < playerList.size(); i++) {
                    playerList.get(i).setRank(i + 1);
                }
                PlayerAdapter adapter = new PlayerAdapter(getActivity(), playerList);
                ranklist_view.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });

        return root;
    }

}

