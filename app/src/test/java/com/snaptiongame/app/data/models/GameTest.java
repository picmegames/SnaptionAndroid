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
        assertTrue(snaption.id == 0);
    }

    @Test
    public void testConstructorSendGame() {
        Game testGame = new Game(0, false, 0, "", "", new ArrayList<>(), new ArrayList<>());
        assertTrue(testGame.userId == 0);
        assertTrue(!testGame.isPublic);
        assertTrue(testGame.rating == 0);
        assertTrue(testGame.picture.isEmpty());
        assertTrue(testGame.type.isEmpty());
        assertTrue(testGame.friendIds.isEmpty());
    }

    @Test
    public void testConstructorGame() {
        Game testGame = new Game(0, 0, false, 0, 0, "", "");
        assertTrue(testGame.id == 0);
    }
}
