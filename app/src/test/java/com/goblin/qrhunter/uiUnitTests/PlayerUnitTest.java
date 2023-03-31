package com.goblin.qrhunter.uiUnitTests;

import com.goblin.qrhunter.Player;

import static org.junit.Assert.*;
import org.junit.Test;

public class PlayerUnitTest {
    private Player player;

    public Player MockPlayer(){
        player = new Player("playerID","playerUserName","playerContact", "ProfileURI", "Phone");
        return player;
    }

    @Test
    public void getIdTest(){
        player = MockPlayer();
        assertEquals(player.getId(), "playerID");
    }

    @Test
    public void setIdTest(){
        player = MockPlayer();
        player.setId("newID");
        assertEquals(player.getId(), "newID");
    }

    @Test
    public void getUsernameTest(){
        player = MockPlayer();
        assertEquals(player.getUsername(), "playerUserName");
    }

    @Test
    public void setUsernameTest(){
        player = MockPlayer();
        player.setUsername("newUser");
        assertEquals(player.getUsername(), "newUser");
    }

    @Test
    public void getContactInfoTest(){
        player = MockPlayer();
        assertEquals(player.getContactInfo(), "playerContact");
    }

    @Test
    public void setContactInfo(){
        player = MockPlayer();
        player.setContactInfo("newInfo");
        assertEquals(player.getContactInfo(), "newInfo");
    }

    @Test
    public void getProfileURI(){
        player = MockPlayer();
        assertEquals(player.getProfileURI(), "ProfileURI");
    }

    @Test
    public void setProfileURI(){
        player = MockPlayer();
        player.setProfileURI("newURI");
        assertEquals(player.getProfileURI(), "newURI");
    }
//    new tests for newly added methods
    @Test
    public void getTotalScoreTest(){
        player = MockPlayer();
        int totalScore = player.getTotalScore();
//        because the new player just created doesn't have score yet
        assertEquals(totalScore, 0);
    }

    @Test
    public void addPointsTest(){
        player = MockPlayer();
        int points = 100;
        player.addPoints(points);
        int addTest = player.getTotalScore();
        assertEquals(addTest, points);
    }

//    new methods -> phone
    @Test
    public void getPhoneTest(){
        player = MockPlayer();
        assertEquals(player.getPhone(), "Phone");
    }

    @Test
    public void setPhoneTest(){
        player = MockPlayer();
        String newPhone = "780-888-9999";
        player.setPhone(newPhone);
        assertEquals(player.getPhone(), newPhone);
    }

    @Test
    public void copyPlayerTests(){
        player = MockPlayer();
        Player player1 = Player.copy(player);
        assertEquals(player1.getId(), player.getId());
        assertEquals(player1.getUsername(), player.getUsername());
        assertEquals(player1.getContactInfo(), player.getContactInfo());
        assertEquals(player1.getPhone(), player.getPhone());
        assertEquals(player1.getProfileURI(), player.getProfileURI());
    }

}
