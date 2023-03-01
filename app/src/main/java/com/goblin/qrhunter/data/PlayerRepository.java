package com.goblin.qrhunter.data;

import com.goblin.qrhunter.Player;

public class PlayerRepository extends FirestoreRepository<Player> {

    protected PlayerRepository(String collectionPath) {
        super(collectionPath, Player.class);
    }

    public PlayerRepository() {
        super("/players", Player.class);
    }
}
