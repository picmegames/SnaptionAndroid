package com.snaptiongame.snaptionapp.data.models;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * @author Tyler Wong
 */

public class LikeTest {
    @Test
    public void testConstructorLike() {
        Like like = new Like(0, 0, false, false, "");
        assertTrue(like.userId == 0);
        assertTrue(like.targetId == 0);
        assertTrue(!like.upvote);
        assertTrue(!like.flagged);
        assertTrue(like.type.isEmpty());
    }
}
