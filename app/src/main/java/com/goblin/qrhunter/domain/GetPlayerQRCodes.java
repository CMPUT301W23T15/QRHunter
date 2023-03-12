package com.goblin.qrhunter.domain;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.goblin.qrhunter.Post;
import com.goblin.qrhunter.QRCode;
import com.goblin.qrhunter.data.PostRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * responsible for getting all QRCodes of a certain user based on their
 * posts and returning a MediatorLiveData object that can be observed by UI components.
 */
public class GetPlayerQRCodes {

    private PostRepository postDB;
    private LiveData<List<Post>> userPosts; // all posts by the user
    private MediatorLiveData<ArrayList<QRCode>> playerCodes = new MediatorLiveData<>();


    public GetPlayerQRCodes(String username) {
        postDB = new PostRepository();
        userPosts = postDB.getUserPosts(username);
        playerCodes.addSource(userPosts, this::refresh);
//        QRCode testCode = new QRCode("I am a test");
//        Post testPost = new Post("Test Post", testCode, username);
//        postDB.add(testPost);
    }

    /**
     * Refreshes the player codes based on the latest post data.
     * @param postList the list of posts to refresh from
     */
    private void refresh(List<Post> postList) {
        QRCode code;
        ArrayList<QRCode> codeList = new ArrayList<>();
        for (Post post : postList) {
            // null guards
            if (post == null || post.getCode() == null || post.getPlayerId() == null) {
                continue;
            }
            // add post hash
            code = post.getCode();
            codeList.add(code);
        }
        playerCodes.setValue(codeList);
    }

    /**
     * Gets the player scores as a live data object.
     * @return the live data object containing the player codes
     */
    public MediatorLiveData<ArrayList<QRCode>> get() {
        return playerCodes;
    }

}
