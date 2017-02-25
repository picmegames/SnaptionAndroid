package com.snaptiongame.snaptionapp.data.models;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * @author Tyler Wong
 */

public class LikeTest {
    @Test
    public void testConstructorLike() {
        Like like = new Like(0, false, Like.FLAGGED, Like.GAME_ID);
        assertTrue(like.targetId == 0);
        assertTrue(!like.choice);
        assertTrue(!like.choiceType.isEmpty());
        assertTrue(!like.type.isEmpty());
    }
}
