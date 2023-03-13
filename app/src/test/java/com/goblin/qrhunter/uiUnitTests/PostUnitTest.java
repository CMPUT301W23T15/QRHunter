package com.goblin.qrhunter.uiUnitTests;

import static org.junit.Assert.*;

import android.location.Location;
import android.util.Log;

import com.goblin.qrhunter.Post;
import com.goblin.qrhunter.QRCode;

import org.junit.Test;

public class PostUnitTest {
    private Post post;
//    not sure how I will use this QRCode to test?
    private QRCode code= new QRCode("test");
    private Location location;

    public Post MockPost(){
        post = new Post("postID","postName","postComments", code, "playerId");
        return post;
    }

    @Test
    public void getIdTest(){
        post = MockPost();
        assertEquals(post.getId(), "postID");
    }

    @Test
    public void setIDTest(){
        post = MockPost();
        post.setId("newPostID");
        assertEquals(post.getId(), "newPostID");
    }

    @Test
    public void getNameTest(){
        post = MockPost();
        assertEquals(post.getName(), "postName");
    }

    @Test
    public void setNameTest(){
        post = MockPost();
        post.setName("newPostName");
        assertEquals(post.getName(), "newPostName");
    }

    @Test
    public void getCodeTest(){
        post = MockPost();
        assertEquals(post.getCode(), code);
    }

    @Test
    public void setCodeTest(){
        post = MockPost();
        QRCode newQRC = new QRCode("someCode");
        post.setCode(newQRC);
        assertEquals(post.getCode(), newQRC);
    }

    @Test
    public void getPlayerIdTest(){
        post = MockPost();
        assertEquals(post.getPlayerId(), "playerId");
    }

    @Test
    public void setPlayerIdTest(){
        post = MockPost();
        post.setPlayerId("newPlayerId");
        assertEquals(post.getPlayerId(), "newPlayerId");
    }

//    NOTE: WE DONT HAVE THESE METHODS ANYMORE
//    @Test
//    public void getLocationTest(){
//        post = MockPost();
//        assertEquals(post.getLocation(), location);
//    }
//
//    @Test
//    public void setLocation(){
//        post = MockPost();
//        Location newLocation = location;
//        post.setLocation(location);
//        assertEquals(post.getLocation(), location);
//    }
    @Test
    public void getPostKeyTest(){
        post = MockPost();
        assertEquals(post.getPostKey(), post.getPlayerId() + code.getHash());
    }

}
