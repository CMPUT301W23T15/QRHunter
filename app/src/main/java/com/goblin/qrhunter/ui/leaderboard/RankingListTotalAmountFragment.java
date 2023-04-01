package com.goblin.qrhunter.ui.leaderboard;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.goblin.qrhunter.Player;
import com.goblin.qrhunter.R;
import com.goblin.qrhunter.databinding.FragmentRankbytotalamountBinding;
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

public class RankingListTotalAmountFragment extends Fragment {
    private static final String TAG = "RankingListTotalAmountFragment";
    private ListView rankListView;
    private TextView textView;
    private RankinglistTotalViewModel viewModel;
    NavController navController;
    private FragmentRankbytotalamountBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(RankinglistTotalViewModel.class);
        navController = Navigation.findNavController(container);
        List<Player> players = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference colRef = db.collection("scores");

        // Fragment Set Up
        binding = FragmentRankbytotalamountBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        Log.d(TAG, "amount");

        // Set up list view
        rankListView = binding.getRoot().findViewById(R.id.listView_totalamount);

        // Retrieve the player total qrcount in descending order and updates the viewmodel
        colRef.orderBy("qrcount", Query.Direction.DESCENDING).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        String playerName = doc.getString("player.username");
                        int qrcount = doc.getLong("qrcount").intValue();
                        // if the user has no QR code
                        Player player = new Player(playerName, qrcount);
                        players.add(player);
                    }

                    // Once all the data is fetched, you can update the player scores in your ViewModel
                    for (Player player : players) {
                        viewModel.updatePlayerScore(player.getUsername(), player.getTotalScore());
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error fetching player qrcount", e);
                });
        // Set the rank for players
        int rank = 1;
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            if (i > 0 && player.getTotalScore() < players.get(i - 1).getTotalScore()) {
                rank++;
            }
            player.setRank(rank);
        }

        // Update the UI with the sorted list of players including their rank
        Collections.sort(players, new Comparator<Player>() {
            @Override
            public int compare(Player p1, Player p2) {
                return Integer.compare(p2.getTotalScore(), p1.getTotalScore());
            }
        });
        for (int i = 0; i < players.size(); i++) {
            players.get(i).setRank(i + 1);
        }

        PlayerAdapter adapter = new PlayerAdapter(requireContext(), players, "amount");
        rankListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        // if ViewModel is updated, regenerate list of player and update adapter
        viewModel.getPlayerScores().observe(getViewLifecycleOwner(), new Observer<Map<String, Integer>>() {
            @Override
            public void onChanged(Map<String, Integer> playerAmounts) {
                List<Player> playerList = new ArrayList<>();
                int rank = 1;
                for (String player : playerAmounts.keySet()) {
                    int amount = playerAmounts.get(player);
                    Player playerObject = new Player(player, amount);
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
                PlayerAdapter adapter = new PlayerAdapter(getActivity(), playerList, "amount");
                rankListView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });

        return root;
    }
}
