package com.snaptiongame.app.data.models;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;

/**
 * @author Tyler Wong
 */

public class GameTest {
    @Test
    public void testDefaultConstructor() {
        Game snaption = new Game();
        assertTrue(snaption.getId() == 0);
    }

    @Test
    public void testConstructorSendGame() {
        Game testGame = new Game(false, "", "", new ArrayList<>(), new ArrayList<>(), 0);
        assertTrue(testGame.getUserId() == 0);
        assertTrue(!testGame.isPublic());
        assertTrue(testGame.getPicture().isEmpty());
        assertTrue(testGame.getType().isEmpty());
        assertTrue(testGame.getFriendIds().isEmpty());
        assertTrue(testGame.getGameDuration() == 0);
    }

    @Test
    public void testConstructorGame() {
        Game testGame = new Game(0, 0, false, 0, "", "");
        assertTrue(testGame.getId() == 0);
    }
}
