/**
 * Stub of ScanViewModel
 */
package com.goblin.qrhunter.ui.scan;

import androidx.lifecycle.ViewModel;

import com.goblin.qrhunter.Player;
import com.goblin.qrhunter.Post;
import com.goblin.qrhunter.QRCode;
import com.goblin.qrhunter.data.PlayerRepository;
import com.goblin.qrhunter.data.PostRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;


/**
 * The ScanViewModel class is responsible for managing
 * and providing data related to the scan screen of the application.
 */
public class ScanViewModel extends ViewModel {
    private Post post;
    private QRCode qrcode;
    private PostRepository postDB;
    private CollectionReference playerCollection;

    /**
     * Constructs a new instance of ScanViewModel and initializes a post with the playerId and gets
     * a reference to the Firebase players collection
     */
    public ScanViewModel() {
        postDB = new PostRepository();
        post = new Post();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        post.setPlayerId(user.getUid());
        PlayerRepository playerRepository = new PlayerRepository();
        playerCollection = playerRepository.getCollectionRef();

    }

    /**
     * Sets the QR code that was scanned in and defaults to null if conditions not met
     * @param qr must be not null and not empty
     */
    public void setQRCode(String qr) {
        if(qr != null && !qr.isEmpty()) {
            qrcode = new QRCode(qr);
            post.setCode(qrcode);
        } else {
            qrcode = null;
            post.setCode(null);
        }
    }

    public CollectionReference getPlayerCollection() {
        return playerCollection;
    }



}