/**
 * This class provides methods to manage Player objects in the Firestore database.
 */
package com.goblin.qrhunter.data;

import com.goblin.qrhunter.Player;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Random;

/**
 * A repository class for managing Player objects in Firestore database.
 * This class extends the `BaseRepository` class, which provides basic CRUD operations for Firestore.
 * It also includes additional methods for retrieving player objects by username and
 * adding a new player with a random username.
 */
public class PlayerRepository extends BaseRepository<Player> {

    /**
     * Constructs a new PlayerRepository object and sets the collection reference and the class type.
     */
    public PlayerRepository() {
        super("players", Player.class);
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

}