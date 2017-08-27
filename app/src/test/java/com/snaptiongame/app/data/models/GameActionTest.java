package com.snaptiongame.app.data.models;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * @author Tyler Wong
 */

public class GameActionTest {
    @Test
    public void testConstructorGameAction() {
        GameAction gameAction = new GameAction(0, false, GameAction.FLAGGED, GameAction.GAME_ID);
        assertTrue(gameAction.getTargetId() == 0);
        assertTrue(!gameAction.getChoice());
        assertTrue(!gameAction.getChoiceType().isEmpty());
        assertTrue(!gameAction.getType().isEmpty());
    }
}
