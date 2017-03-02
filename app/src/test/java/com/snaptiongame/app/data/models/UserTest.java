package com.snaptiongame.app.data.models;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * @author Tyler Wong
 */

public class UserTest {
    @Test
    public void testDefaultConstructor() {
        User user = new User();
        assertTrue(user.username == null);
    }

    @Test
    public void testConstructorUser() {
        User userUsername = new User("");
        assertTrue(userUsername.username.isEmpty());

        User userPicture = new User("", "");
        assertTrue(userPicture.picture.isEmpty());
        assertTrue(userPicture.type.isEmpty());
    }
}
