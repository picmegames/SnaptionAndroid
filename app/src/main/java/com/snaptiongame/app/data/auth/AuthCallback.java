package com.snaptiongame.app.data.auth;

/**
 * @author Tyler Wong
 */

public interface AuthCallback {
    void onAuthSuccess();
    void onAuthFailure();
}
