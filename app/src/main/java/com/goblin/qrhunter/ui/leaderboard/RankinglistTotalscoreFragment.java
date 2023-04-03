package com.goblin.qrhunter.ui.leaderboard;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.collection.ArraySet;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.goblin.qrhunter.MainActivity;
import com.goblin.qrhunter.Player;
import com.goblin.qrhunter.Post;
import com.goblin.qrhunter.R;
import com.goblin.qrhunter.databinding.FragmentRankbytotalscoreBinding;
import com.goblin.qrhunter.databinding.FragmentSearchBinding;
import com.goblin.qrhunter.domain.GetPlayersScoreUseCase;
import com.goblin.qrhunter.ui.search.SearchViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.util.Log;


/**
 * A Fragment subclass for displaying the leaderboard of players based on their total score.
 * This fragment retrieves the player scores ordered by the total score in descending order
 * and updates the player scores in the ViewModel. It displays a list of players with their
 * ranks and scores sorted by total score in descending order.
 */
public class RankinglistTotalscoreFragment extends Fragment {

    // private GetPlayersScoreUseCase getPlayersScoreUseCase;
    private static final String TAG = "RankinglistTotalFrag";

    private ListView ranklist_view;
    private RankinglistTotalViewModel viewModel;
    NavController navController;
    private FragmentRankbytotalscoreBinding binding;
    private TextView textView_current_rank;


    /**
     * Creates the view for the fragment and initializes the ViewModel, NavController,
     * and data binding. Retrieves the player scores ordered by the total score in descending order
     * and updates the player scores in the ViewModel. It displays a list of players with their
     * ranks and scores sorted by total score in descending order.
     *
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return The inflated view for the fragment
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(RankinglistTotalViewModel.class);
        navController = Navigation.findNavController(container);


        // Fragment Set Up
        binding = FragmentRankbytotalscoreBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        //Log.d(TAG, "scores");

        // Set up list view
        ranklist_view = binding.getRoot().findViewById(R.id.listView_totalscore);
        //textView_current_rank=binding.getRoot().findViewById(R.id.user_current_score_rank);


        //  Create a list of players
        List<Player> players = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference scoresRef = db.collection("scores");

        // Retrieves the player scores ordered by the total score in descending order
        // and updates the player scores in the ViewModel
        scoresRef.orderBy("totalScore", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        String playerName = document.getString("player.username");
                        int totalScore = document.getLong("totalScore").intValue();

                        //if the user has no QR code

                        Player player = new Player(playerName, totalScore);
                        players.add(player);
                    }
                    // Set the rank for each player
                    int rank = 1;
                    for (Player player : players) {
                        player.setRank(rank);
                        rank++;
                    }
                    // Sort the list based on the total score
                    Collections.sort(players, new Comparator<Player>() {
                        /**
                         * Compares two Player objects based on their total scores in descending order.
                         *
                         * @param p1 The first player object to be compared
                         * @param p2 The second player object to be compared
                         *
                         * @return A positive integer if p1's total score is less than p2's, zero if they're equal,
                         * or a negative integer if p1's total score is greater than p2's.
                         */
                        @Override
                        public int compare(Player p1, Player p2) {
                            return Integer.compare(p2.getTotalScore(), p1.getTotalScore());
                        }
                    });
                    // Update the UI with the sorted list of players including their rank
                    List<Map<String, String>> data = new ArrayList<>();
                    for (int i = 0; i < players.size(); i++) {
                        Player player = players.get(i);
                        Map<String, String> map = new HashMap<>();
                        map.put("rank", String.valueOf(player.getRank()));
                        map.put("name", player.getUsername());
                        map.put("score", String.valueOf(player.getTotalScore()));
                        data.add(map);
                    }
                    PlayerAdapter adapter = new PlayerAdapter(requireContext(), players);
                    ranklist_view.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                });

        // if scores are updated in ViewModel
        // re-generate the list of players and update the ListView adapter
        viewModel.getPlayerScores().observe(getViewLifecycleOwner(), new Observer<Map<String, Integer>>() {
            /**
             * Called when the data is changed.
             *
             * @param playerScores  The new data
             */
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
                    /**
                     * Compares two Player objects based on their total scores in descending order.
                     *
                     * @param p1 The first player object to be compared
                     * @param p2 The second player object to be compared
                     *
                     * @return A positive integer if p1's total score is less than p2's, zero if they're equal,
                     * or a negative integer if p1's total score is greater than p2's.
                     */
                    @Override
                    public int compare(Player p1, Player p2) {
                        return Integer.compare(p2.getTotalScore(), p1.getTotalScore()); // Sort in descending order
                    }
                });
                for (int i = 0; i < playerList.size(); i++) {
                    playerList.get(i).setRank(i + 1);
                }
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                String uid = currentUser.getUid();

                DatabaseReference playerRef = FirebaseDatabase.getInstance().getReference("players").child(uid);
                playerRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    /**
                     * This method will be called with a snapshot of the data at this location.
                     * It will also be called each time that data changes
                     *
                     * @param dataSnapshot The current data at the location
                     */
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Player currentPlayer = dataSnapshot.getValue(Player.class);
                        if (currentPlayer != null) {
                            int currentRank = currentPlayer.getRank();
                            textView_current_rank.setText(String.valueOf(currentRank));
                            Log.d(TAG, "Current player's rank: " + currentRank);
                        }
                    }

                    /**
                     * This method will be triggered in the event that this listener either failed at
                     * the server, or is removed as a result of the security and Firebase rules.
                     *
                     * @param databaseError A description of the error that occurred
                     */
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e(TAG, "Failed to retrieve player data", databaseError.toException());
                    }
                });
                PlayerAdapter adapter = new PlayerAdapter(getActivity(), playerList);
                ranklist_view.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            }
        });

        return root;
    }

}

