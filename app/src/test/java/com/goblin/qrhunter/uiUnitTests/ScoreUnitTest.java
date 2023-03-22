package com.goblin.qrhunter.uiUnitTests;

import com.goblin.qrhunter.QRCode;

import java.util.List;
import com.goblin.qrhunter.Score;

import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.internal.matchers.Null;

public class ScoreUnitTest {
    private Score score;

    private int totalScore;

    QRCode qrCodeHelloWorld = new QRCode("Hello World!"); // score is 211
    QRCode anotherQR = new QRCode("First Code! Hashed"); // score is 191
    
    private List<QRCode> posts;

    private String playerId = "playerID";

    public Score MockScore(){
        score = new Score(totalScore, posts, playerId);
        return score;
    }

    @Test
    public void getTotalScore(){
        int sum = 0;
        score = MockScore();
        assertEquals(score.getHighestScore(),0);
        score.addQRCode(qrCodeHelloWorld);
        // keep track of sum make sure its right
        sum += score.getHighestScore();
        assertEquals(sum, score.getTotalScore());
        score.addQRCode(anotherQR);
//        picking one that is lower, assert sums are equal
        sum += score.getLowestScore();
        assertEquals(sum, score.getTotalScore());
    }

    @Test
    public void getPostsTest() throws NullPointerException {
        score = MockScore();
        assertNull(score.getPosts());
        score.addQRCode(qrCodeHelloWorld);
        assertNotNull(score.getPosts());
        assertEquals(score.getPosts().size(), 1);
        score.addQRCode(anotherQR);
        assertEquals(score.getPosts().size(), 2);
    }

    @Test
    public void getPlayerIdTest(){
        score = MockScore();
        assertEquals(score.getPlayerId(), playerId);
    }

    @Test
    public void setPlayerIDTest(){
        score = MockScore();
        score.setPlayerId("newID");
        assertEquals(score.getPlayerId(), "newID");
    }

    @Test
    public void addQRCodeAndCountTest() throws NullPointerException {
        score = MockScore();
        score.addQRCode(qrCodeHelloWorld);
        assertEquals(score.getQRCount(), 1);
    }

    @Test
    public void removeQRCodeAndCountTest() throws NullPointerException{
        score = MockScore();
        score.addQRCode(qrCodeHelloWorld);
        score.removeQRCode(qrCodeHelloWorld);
        assertEquals(score.getQRCount(), 0);
    }

//    don't need to test sort posts

    @Test
    public void getHighestScoreTest(){
        score = MockScore();
        assertEquals(score.getHighestScore(),0);
//        manually checked, score is 191
        score.addQRCode(anotherQR);
        assertEquals(score.getHighestScore(),191);
//        adding another qr code with a higher score
        score.addQRCode(qrCodeHelloWorld);
        assertEquals(score.getHighestScore(),211);
    }

    @Test
    public void getLowestScoreTest(){
        score = MockScore();
        score.addQRCode(anotherQR); //191
        score.addQRCode(qrCodeHelloWorld); //211
        assertEquals(score.getLowestScore(), 191);
        score.removeQRCode(anotherQR);
        assertEquals(score.getLowestScore(), 211);
    }

//    slightly redundant as its same as get/setPlayerID
    @Test
    public void getIdTest(){
        score = MockScore();
        assertEquals(score.getId(), playerId);
    }

    @Test
    public void setIdTest(){
        score = MockScore();
        score.setId("NewID");
        assertEquals(score.getId(), "NewID");
    }
}
