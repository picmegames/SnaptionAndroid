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
    ImageView mLogo;
    @BindView(R.id.facebook_login_button)
    LoginButton mFacebookLoginButton;
    @BindView(R.id.google_sign_in_button)
    SignInButton mGoogleSignInButton;

    private AuthManager mAuthManager;
    private LoginContract.Presenter mPresenter;

    private static final int RC_SIGN_IN = 2222;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        // Initialize Authentication Manager
        mAuthManager = AuthManager.getInstance();
        mAuthManager.registerCallback(this);
        mAuthManager.setFacebookCallback(mFacebookLoginButton);
        mPresenter = new LoginPresenter(this);
    }

    @OnClick(R.id.facebook_login_button_styled)
    public void facebookLogin() {
        mFacebookLoginButton.performClick();
    }

    @OnClick(R.id.google_sign_in_button_styled)
    public void googleSignIn() {
        startActivityForResult(mAuthManager.getGoogleIntent(), RC_SIGN_IN);
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
        mAuthManager.connectGoogleApi();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuthManager.disconnectGoogleApi();
    }

    @Override
    public void onBackPressed() {
        mAuthManager.registerCallback(null);
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void setPresenter(LoginContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            mAuthManager.googleActivityResult(data);
        }
        else {
            mAuthManager.facebookActivityResult(requestCode, resultCode, data);
        }
    }
}
