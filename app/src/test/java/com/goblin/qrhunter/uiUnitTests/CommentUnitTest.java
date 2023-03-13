package com.goblin.qrhunter.uiUnitTests;

import com.goblin.qrhunter.Comment;
import static org.junit.Assert.*;
import org.junit.Test;

public class CommentUnitTest {
    private Comment comment;

//    mockComment used for testing given the parameters
    public Comment MockComment() {
        comment = new Comment("userID", "Player22", "PostID", "Comment Text");
        return comment;
    }

    @Test
    public void getIdTest() {
        comment = MockComment();
        assertEquals(comment.getId(), "userID");
    }

    @Test
    public void setIdTest() {
        comment = MockComment();
        comment.setId("set");
        assertEquals(comment.getId(), "set");
    }

    @Test
    public void getPlayerIdTest(){
        comment = MockComment();
        assertEquals(comment.getPlayerId(),"Player22");
    }

    @Test
    public void setPlayerIdTest(){
        comment = MockComment();
        comment.setPlayerId("New PlayerID");
        assertEquals(comment.getPlayerId(), "New PlayerID");
    }

    @Test
    public void getPostIdTest(){
        comment = MockComment();
        assertEquals(comment.getPostId(), "PostID");
    }

    @Test
    public void setPostIdTest(){
        comment = MockComment();
        comment.setPostId("New PostId");
        assertEquals(comment.getPostId(), "New PostId");
    }

    @Test
    public void getText(){
        comment = MockComment();
        assertEquals(comment.getText(), "Comment Text");
    }

    @Test
    public void setText(){
        comment = MockComment();
        comment.setText("New Text");
        assertEquals(comment.getText(), "New Text");
    }
}


