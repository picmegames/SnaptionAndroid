package com.snaptiongame.app.data.models;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 * @author Nick Romero
 */

public class CaptionSetTest {

    CaptionSet testSet;
    List<Caption> captions;

    @Before
    public void setup() {
        captions = new ArrayList<>();
        captions.add(new Caption(0, "First caption"));
        captions.add(new Caption(1, "Second caption"));
        captions.add(new Caption(2, "Third caption"));

        testSet = new CaptionSet();
        testSet.setSetName("Welcome to the thunderdome.");
    }

    @Test
    public void testDefaultConstructor() {
        assertNotNull(testSet);
    }

    @Test
    public void testGetTotalCaptions() {
        assertEquals(CaptionSet.MAX_CAPTIONS, 10);
    }

    @Test
    public void testGetCaptionsUnlocked() {
        assertEquals(CaptionSet.MIN_CAPTIONS, 0);
    }

    @Test
    public void testGetSetName() {
        assertTrue(testSet.getSetName().equals("Welcome to the thunderdome."));
    }



}
