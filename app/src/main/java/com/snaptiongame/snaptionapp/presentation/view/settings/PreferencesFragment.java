package com.snaptiongame.snaptionapp.presentation.view.settings;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.view.View;
import android.widget.ListView;

import com.snaptiongame.snaptionapp.R;
import com.snaptiongame.snaptionapp.data.authentication.AuthenticationManager;
import com.snaptiongame.snaptionapp.presentation.view.login.LoginActivity;

import timber.log.Timber;

/**
 * @author Tyler Wong
 */
public class PreferencesFragment extends PreferenceFragment implements
        Preference.OnPreferenceClickListener {
    private Preference mVersionPreference;
    private Preference mLogoutPreference;

    private AuthenticationManager mAuthManager;
    private boolean mListStyled = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuthManager = AuthenticationManager.getInstance();
        mAuthManager.registerCallback(this::updateLoginField);

        View rootView = getView();
        ListView list = null;
        if (rootView != null) {
            list = (ListView) rootView.findViewById(android.R.id.list);
        }
        if (list != null) {
            list.setDivider(null);
        }

        PackageInfo packageInfo = null;

        try {
            packageInfo = getActivity().getPackageManager().getPackageInfo(
                    getActivity().getPackageName(), 0);
        }
        catch (PackageManager.NameNotFoundException e) {
            Timber.e(e);
        }

        addPreferencesFromResource(R.xml.preferences);

        mVersionPreference = getPreferenceScreen().findPreference(getString(R.string.version_label));
        mLogoutPreference = getPreferenceScreen().findPreference(getString(R.string.log_out_label));
        mLogoutPreference.setOnPreferenceClickListener(this);

        updateLoginField();

        if (packageInfo != null) {
            mVersionPreference.setSummary(packageInfo.versionName);
        }
    }

    protected void updateLoginField() {
        if (mAuthManager.isLoggedIn()) {
            mLogoutPreference.setTitle(R.string.log_out_label);
            mLogoutPreference.setSummary(String.format(getString(R.string.current_login), mAuthManager.getSnaptionUsername()));
        }
        else {
            mLogoutPreference.setTitle(R.string.log_in_label);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!mListStyled) {
            View rootView = getView();
            if (rootView != null) {
                ListView list = (ListView) rootView.findViewById(android.R.id.list);
                list.setPadding(0, 0, 0, 0);
                list.setDivider(null);
                mListStyled = true;
            }
        }
    }

    private void goToLogin() {
        Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
        startActivity(loginIntent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mAuthManager.unregisterCallback();
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if (preference.getKey().equals(getString(R.string.log_out_label))) {
            if (mAuthManager.isLoggedIn()) {
                mAuthManager.logout();
            }
            goToLogin();
        }
        return true;
    }
}
