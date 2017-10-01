package com.snaptiongame.app.presentation.view.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

import com.snaptiongame.app.R
import com.snaptiongame.app.data.auth.AuthCallback
import com.snaptiongame.app.data.auth.AuthManager

import kotlinx.android.synthetic.main.activity_login.*

/**
 * @author Tyler Wong
 */

class LoginActivity : AppCompatActivity(), LoginContract.View, AuthCallback {

    private val authManager: AuthManager = AuthManager.getInstance()
    private var presenter: LoginContract.Presenter = LoginPresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize Authentication Manager
        authManager.registerCallback(this)
        authManager.setFacebookCallback(facebookLoginButton)

        facebookLoginButtonStyled.setOnClickListener { facebookLoginButton.performClick() }
        googleSignInButtonStyled.setOnClickListener { startActivityForResult(authManager.googleIntent, RC_SIGN_IN) }
    }

    override fun onAuthSuccess() {
        setResult(Activity.RESULT_OK)
        finish()
    }

    override fun onAuthFailure() {

    }

    override fun onStart() {
        super.onStart()
        authManager.connectGoogleApi()
    }

    override fun onStop() {
        super.onStop()
        authManager.disconnectGoogleApi()
    }

    override fun onBackPressed() {
        authManager.registerCallback(null)
        setResult(Activity.RESULT_OK)
        finish()
    }

    override fun setPresenter(presenter: LoginContract.Presenter) {
        this.presenter = presenter
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            authManager.googleActivityResult(data)
        }
        else {
            authManager.facebookActivityResult(requestCode, resultCode, data)
        }
    }

    companion object {
        private val RC_SIGN_IN = 2222
    }
}
