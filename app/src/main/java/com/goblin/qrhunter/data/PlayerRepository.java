package com.goblin.qrhunter.data;

import androidx.annotation.NonNull;

import com.goblin.qrhunter.Player;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;
import java.util.Random;

public class PlayerRepository extends BaseRepository<Player> {

    public PlayerRepository() {
        super("players", Player.class);
    }


    public Task<Player> getPlayerByUsername(String username) {
        return getCollectionRef()
                .whereEqualTo("username", username)
                .get()
                .continueWith(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot.size() > 0) {
                            DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);
                            Player player = documentSnapshot.toObject(Player.class);
                            player.setId(documentSnapshot.getId());
                            return player;
                        }
                    }
                    return null;
                });
    }

    private Task<String> randomUsername() {
        String username = "User" + new Random().nextInt(999999999);
        return getPlayerByUsername(username).continueWith(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                return randomUsername().getResult();
            } else {
                return username;
            }
        });
    }

    public Task<Void> addWithRandomUsername(String id) {

        Task<String> usernameTask = this.randomUsername();

        // Add a continuation to create the Player object when the username is available
        return usernameTask.continueWithTask(task -> {
            if (task.isSuccessful()) {
                String username = task.getResult();
                Player player = new Player(id, username, "example@email.com");
                return add(player);
            } else {
                // Return the exception as a task
                return Tasks.forException(task.getException());
            }
        });
    }

}