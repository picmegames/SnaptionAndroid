package com.snaptiongame.app.data.models;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;

/**
 * @author Tyler Wong
 */

public class SnaptionTest {
    @Test
    public void testDefaultConstructor() {
        Snaption snaption = new Snaption();
        assertTrue(snaption.id == 0);
    }

    @Test
    public void testConstructorSendSnaption() {
        Snaption testSnaption = new Snaption(0, false, 0, "", "", new ArrayList<>(), new ArrayList<>());
        assertTrue(testSnaption.userId == 0);
        assertTrue(!testSnaption.isPublic);
        assertTrue(testSnaption.rating == 0);
        assertTrue(testSnaption.picture.isEmpty());
        assertTrue(testSnaption.type.isEmpty());
        assertTrue(testSnaption.friendIds.isEmpty());
    }

    @Test
    public void testConstructorSnaption() {
        Snaption testSnaption = new Snaption(0, 0, false, 0, 0, "", "");
        assertTrue(testSnaption.id == 0);
    }
}