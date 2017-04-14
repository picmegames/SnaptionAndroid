package com.snaptiongame.app.presentation.view.settings;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.snaptiongame.app.R;
import com.snaptiongame.app.SnaptionApplication;
import com.snaptiongame.app.data.auth.AuthManager;
import com.snaptiongame.app.data.utils.CacheUtils;
import com.snaptiongame.app.presentation.view.login.LoginActivity;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * @author Tyler Wong
 */
public class PreferencesFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener {
    private Preference mCachePreference;
    private Preference mLogoutPreference;
    private Preference mVersionPreference;

    private AuthManager mAuthManager;
    private boolean mListStyled = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuthManager = AuthManager.getInstance();

        View rootView = getView();
        ListView list = null;
        if (rootView != null) {
            list = (ListView) rootView.findViewById(android.R.id.list);
        }
        if (list != null) {
            list.setDivider(ContextCompat.getDrawable(SnaptionApplication.getContext(), R.drawable.line_divider));
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

        mCachePreference = getPreferenceScreen().findPreference(getString(R.string.clear_cache));
        mCachePreference.setOnPreferenceClickListener(this);

        mLogoutPreference = getPreferenceScreen().findPreference(getString(R.string.log_out_label));
        mLogoutPreference.setOnPreferenceClickListener(this);
        mVersionPreference = getPreferenceScreen().findPreference(getString(R.string.version_label));

        updateCacheSummary();
        updateLoginSummary();

        if (packageInfo != null) {
            mVersionPreference.setSummary(packageInfo.versionName);
        }
    }

    private void updateCacheSummary() {
        mCachePreference.setSummary(
                String.format(getString(R.string.current_size), CacheUtils.getCacheSize()));
    }

    private void updateLoginSummary() {
        if (AuthManager.isLoggedIn()) {
            mLogoutPreference.setTitle(R.string.log_out_label);
            mLogoutPreference.setSummary(String.format(getString(R.string.current_login),
                    AuthManager.getUsername()));
        }
        else {
            mLogoutPreference.setTitle(R.string.log_in_label);
            mLogoutPreference.setSummary("");
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
                list.setDivider(ContextCompat.getDrawable(SnaptionApplication.getContext(), R.drawable.line_divider));
                mListStyled = true;
            }
        }
        updateCacheSummary();
    }

    private void goToLogin() {
        Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
        startActivity(loginIntent);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if (preference.getKey().equals(getString(R.string.log_out_label))) {
            if (AuthManager.isLoggedIn()) {
                mAuthManager.logout();
                updateLoginSummary();
            }
            goToLogin();
            getActivity().finish();
        }
        else if (preference.getKey().equals(getString(R.string.clear_cache))) {
            CacheUtils.clearCache()
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> {
                                Toast.makeText(SnaptionApplication.getContext(), getString(R.string.clear_cache_success), Toast.LENGTH_LONG).show();
                                mCachePreference.setSummary(String.valueOf(CacheUtils.getCacheSize()));
                            },
                            e -> {
                                Timber.e(e);
                                Toast.makeText(SnaptionApplication.getContext(), getString(R.string.clear_cache_fail), Toast.LENGTH_LONG).show();
                            });
        }
        return true;
    }
}
