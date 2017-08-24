package com.snaptiongame.app.presentation.view.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.SignInButton;
import com.snaptiongame.app.R;
import com.snaptiongame.app.data.auth.AuthCallback;
import com.snaptiongame.app.data.auth.AuthManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Tyler Wong
 */

public class LoginActivity extends AppCompatActivity implements LoginContract.View, AuthCallback {
    @BindView(R.id.logo)
    ImageView logo;
    @BindView(R.id.facebook_login_button)
    LoginButton facebookLoginButton;
    @BindView(R.id.google_sign_in_button)
    SignInButton googleSignInButton;

    private AuthManager authManager;
    private LoginContract.Presenter presenter;

    private static final int RC_SIGN_IN = 2222;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        // Initialize Authentication Manager
        authManager = AuthManager.getInstance();
        authManager.registerCallback(this);
        authManager.setFacebookCallback(facebookLoginButton);
        presenter = new LoginPresenter(this);
    }

    @OnClick(R.id.facebook_login_button_styled)
    public void facebookLogin() {
        facebookLoginButton.performClick();
    }

    @OnClick(R.id.google_sign_in_button_styled)
    public void googleSignIn() {
        startActivityForResult(authManager.getGoogleIntent(), RC_SIGN_IN);
    }

    @Override
    public void onAuthSuccess() {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onAuthFailure() {

    }

    @Override
    protected void onStart() {
        super.onStart();
        authManager.connectGoogleApi();
    }

    @Override
    protected void onStop() {
        super.onStop();
        authManager.disconnectGoogleApi();
    }

    @Override
    public void onBackPressed() {
        authManager.registerCallback(null);
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void setPresenter(LoginContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            authManager.googleActivityResult(data);
        }
        else {
            authManager.facebookActivityResult(requestCode, resultCode, data);
        }
    }
}
