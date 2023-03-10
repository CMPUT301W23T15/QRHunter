package com.goblin.qrhunter.uiTests;

import com.goblin.qrhunter.Player;

import static org.junit.Assert.*;
import org.junit.Test;

public class PlayerUnitTest {
    private Player player;

    public Player MockPlayer(){
        player = new Player("playerID","playerUserName","playerContact", "ProfileURI");
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
}
