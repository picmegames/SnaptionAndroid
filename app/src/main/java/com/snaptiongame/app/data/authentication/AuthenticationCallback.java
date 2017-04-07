package com.snaptiongame.app.data.authentication;

/**
 * @author Tyler Wong
 */

public interface AuthenticationCallback {
    void onAuthenticationSuccess();
    void onAuthenticationFailure();
}
