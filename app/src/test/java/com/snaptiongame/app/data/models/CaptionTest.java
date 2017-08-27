package com.snaptiongame.app.data.models;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Tyler Wong
 */

public class CaptionTest {
    @Test
    public void testDefaultConstructor() {
        Caption caption = new Caption();
        assertTrue(caption.getId() == 0);
    }

    @Test
    public void testConstructorSendCaption() {
        Caption caption = new Caption(0, "");
        assertTrue(caption.getFitBId() == 0);
        assertTrue(caption.getCaption().isEmpty());
    }

    @Test
    public void testConstructorCaption() {
        Caption caption = new Caption(0, 0, 0, 0, 0, "", new FitBCaption(0, 0, "", "", 0));
        assertTrue(caption.getId() == 0);
    }

    @Test
    public void testEqualsCaptionTrue() {
        Caption caption1 = new Caption(32, "I'm a caption");
        caption1.setNumVotes(200);
        caption1.setCreatorName("hi");
        caption1.setCreatorPicture("http://url.com");
        Caption caption2 = new Caption(32, "I'm a caption");
        caption2.setNumVotes(200);
        caption2.setCreatorName("hi");
        caption2.setCreatorPicture("http://url.com");
        assertTrue(caption1.equals(caption2));
    }

    @Test
    public void testEqualsCaptionFalse() {
        Caption caption1 = new Caption(32, "I'm a caption");
        caption1.setNumVotes(200);
        Caption caption2 = new Caption(32, "I'm a caption");
        caption1.setNumVotes(1234567);
        assertFalse(caption1.equals(caption2));

    }
}
