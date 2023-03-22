/**
 * This class provides methods to manage Player objects in the Firestore database.
 */
package com.goblin.qrhunter.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.goblin.qrhunter.Player;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.Transaction;

import java.util.Random;

/**
 * A repository class for managing Player objects in Firestore database.
 * This class extends the `BaseRepository` class, which provides basic CRUD operations for Firestore.
 * It also includes additional methods for retrieving player objects by username and
 * adding a new player with a random username.
 */
public class PlayerRepository extends BaseRepository<Player> {

    private final FirebaseFirestore db;

    private final CollectionReference scoreCol;

    /**
     * Constructs a new PlayerRepository object and sets the collection reference and the class type.
     */
    public PlayerRepository() {
        super("players", Player.class);
        this.db = FirebaseFirestore.getInstance();
        scoreCol = this.db.collection("scores");

    }


    /**
     * Updates the player in the collection.
     * @param player The new model object to replace the old one with.
     * @return A task indicating whether the operation was successful.
     */
    @Override
    public Task<Void> update(@NonNull Player player) {
        if(player.getId() == null || player.getId().isEmpty()) {
            return Tasks.forException(new IllegalArgumentException("Invalid player argument"));
        }
        DocumentReference playerRef = getCollectionRef().document(player.getId());
        DocumentReference scoreRef = getScoreCol().document(player.getId());

        return db.runTransaction(transaction -> {
            transaction.update(playerRef, player.toMap());
            transaction.set(scoreRef, player.toMap(), SetOptions.merge());
            return null;
        });
    }

    /**
     * Gets a player object by its username.
     * @param username the username of the player to retrieve.
     * @return a task that returns the player object if successful, or null if the player does not exist.
     */
//     change to public for testing?
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

    /**
     * Generates a random unique username for a new player.
     *
     * @return A task that returns the random username if successful, or an exception if not.
     */
    public Task<String> randomUsername() {
        String username = "User" + new Random().nextInt(999999999);
        return getPlayerByUsername(username).continueWith(task -> {
            // if exist retry
            if (task.isSuccessful() && task.getResult() != null) {
                return randomUsername().getResult();
            } else {
                return username;
            }
        });
    }

    /**
     * Adds a new player object with a random unique username and the given ID. Primarly used for
     * first time signin.
     * @param id the ID of the new player object.
     * @return a task that returns null if successful, or an exception if not.
     */
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


    /**
     * Gets a player object by its ID.
     * @param id the ID of the player to retrieve.
     * @return a task that returns the player object if successful, or null if the player does not exist.
     */
    public Task<Player> getPlayerById(String id) {
        return this.get(id);
    }

    /**
     * get the score collection reference.
     * @return CollectionReference of the score table
     */
    private CollectionReference getScoreCol() {
        return this.scoreCol;
    }
}