/**
 * Stub of ScanViewModel
 */
package com.goblin.qrhunter.ui.scan;

import androidx.lifecycle.ViewModel;

import com.goblin.qrhunter.Player;
import com.goblin.qrhunter.Post;
import com.goblin.qrhunter.QRCode;
import com.goblin.qrhunter.data.PostRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


/**
 * Stub of ScanViewModel
 */
public class ScanViewModel extends ViewModel {
    private Post post;
    private QRCode qrcode;
    private PostRepository postDB;

    public ScanViewModel() {
        postDB = new PostRepository();
        post = new Post();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        post.setPlayerId(user.getUid());
    }

    public void setQRCode(String qr) {
        if(qr != null && !qr.isEmpty()) {
            qrcode = new QRCode(qr);
            post.setCode(qrcode);
        } else {
            qrcode = null;
            post.setCode(null);
        }
    }



}