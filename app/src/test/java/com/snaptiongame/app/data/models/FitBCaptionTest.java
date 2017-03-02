package com.snaptiongame.app.data.models;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Created by nickromero on 2/20/17.
 */

public class FitBCaptionTest {

    @Test
    public void testDefaultConstructor() {
        FitBCaption caption = new FitBCaption(0, 200, "Before", "After", 12345);
        assertTrue(caption.id == 0);
        assertTrue(caption.beforeBlank.equals("Before"));
        assertTrue(caption.afterBlank.equals("After"));
        assertTrue(caption.setID == 12345);
        assertTrue(caption.charLimit == 200);
    }
}
