package com.snaptiongame.app.data.models;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * @author Tyler Wong
 */

public class SessionTest {
    @Test
    public void testConstructorSession() {
        Session session = new Session(0);
        assertTrue(session.userId == 0);
    }
}
