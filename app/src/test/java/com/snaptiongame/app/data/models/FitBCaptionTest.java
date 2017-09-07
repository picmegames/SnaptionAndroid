package com.snaptiongame.app.data.models;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * @author Nick Romero
 */

public class FitBCaptionTest {

    @Test
    public void testDefaultConstructor() {
        FitBCaption caption = new FitBCaption(0, 200, "Before", "After", 12345);
        assertTrue(caption.getId() == 0);
        assertTrue(caption.getBeforeBlank().equals("Before"));
        assertTrue(caption.getAfterBlank().equals("After"));
        assertTrue(caption.getSetId() == 12345);
        assertTrue(caption.getCharLimit() == 200);
    }
}
