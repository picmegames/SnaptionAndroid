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
        assertTrue(caption.id == 0);
        assertTrue(caption.beforeBlank.equals("Before"));
        assertTrue(caption.afterBlank.equals("After"));
        assertTrue(caption.setId == 12345);
        assertTrue(caption.charLimit == 200);
    }
}
