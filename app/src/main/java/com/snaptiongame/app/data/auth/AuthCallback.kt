package com.snaptiongame.app.data.auth

/**
 * @author Tyler Wong
 */

interface AuthCallback {
    fun onAuthSuccess()

    fun onAuthFailure()
}
