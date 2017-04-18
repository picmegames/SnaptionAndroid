package com.snaptiongame.app.presentation.view.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
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

        // Initialize Authentication Manager
        mAuthManager = AuthManager.getInstance();
        mAuthManager.registerCallback(this);

        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        // Set Logo
        Glide.with(this)
                .load(R.mipmap.ic_launcher)
                .fitCenter()
                .into(mLogo);

        mAuthManager.setFacebookCallback(mFacebookLoginButton);

        mPresenter = new LoginPresenter(this);
    }

    @Override
    public void onAuthSuccess() {
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
        finish();
    }

    @Override
    public void setPresenter(LoginContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @OnClick(R.id.google_sign_in_button)
    public void googleLogin(View view) {
        startActivityForResult(mAuthManager.getGoogleIntent(), RC_SIGN_IN);
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
