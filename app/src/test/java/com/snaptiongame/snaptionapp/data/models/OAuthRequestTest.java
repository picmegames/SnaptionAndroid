package com.snaptiongame.snaptionapp.data.models;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * @author Tyler Wong
 */

public class OAuthRequestTest {
    @Test
    public void testConstructorOAuthRequest() {
        OAuthRequest request = new OAuthRequest("", "", "");
        assertTrue(request.token.isEmpty());
        assertTrue(request.deviceToken.isEmpty());
    }
}
