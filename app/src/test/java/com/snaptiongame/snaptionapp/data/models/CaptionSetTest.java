package com.snaptiongame.snaptionapp.data.models;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 * Created by nickromero on 2/20/17.
 */

public class CaptionSetTest {

    CaptionSet testSet;
    List<Caption> captions;

    @Before
    public void setup() {
        captions = new ArrayList<>();
        captions.add(new Caption(0, "First caption", 123));
        captions.add(new Caption(1, "Second caption", 234));
        captions.add(new Caption(2, "Third caption", 345));

        testSet = new CaptionSet();
        testSet.captionSetTitle = "Welcome to the thunderdome.";
    }

    @Test
    public void testDefaultConstructor() {
        assertNotNull(testSet);
    }

    @Test
    public void testGetTotalCaptions() {
        assertEquals(testSet.getTotalCaptions(), 10);
    }

    @Test
    public void testGetCaptionsUnlocked() {
        assertEquals(testSet.getCaptionsUnlocked(), 0);
    }

    @Test
    public void testGetSetName() {
        assertTrue(testSet.getSetName().equals("Welcome to the thunderdome."));
    }



}
