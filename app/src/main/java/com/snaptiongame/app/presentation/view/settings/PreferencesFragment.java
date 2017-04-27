package com.snaptiongame.app.presentation.view.settings;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.snaptiongame.app.R;
import com.snaptiongame.app.SnaptionApplication;
import com.snaptiongame.app.data.auth.AuthManager;
import com.snaptiongame.app.presentation.view.login.LoginActivity;

import timber.log.Timber;

/**
 * @author Tyler Wong
 */
public class PreferencesFragment extends PreferenceFragment implements PreferencesContract.View,
        Preference.OnPreferenceClickListener {
    private Preference mCachePreference;
    private Preference mLogoutPreference;
    private Preference mVersionPreference;
    private Preference mFeedbackPreference;
    private PreferenceCategory mNotificationsCategory;
    private SwitchPreference mGameNotificationsPreference;
    private SwitchPreference mFriendNotificationsPreference;

    private PreferencesContract.Presenter mPresenter;
    private PreferenceScreen mPreferenceScreen;

    private boolean mListStyled = false;

    public static PreferencesFragment newInstance() {
        return new PreferencesFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new PreferencesPresenter(this);

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

        mPreferenceScreen = getPreferenceScreen();

        mNotificationsCategory = (PreferenceCategory) mPreferenceScreen.findPreference(
                getString(R.string.notifications));
        mGameNotificationsPreference = (SwitchPreference) mPreferenceScreen.findPreference(
                getString(R.string.game_notifications));
        mFriendNotificationsPreference = (SwitchPreference) mPreferenceScreen.findPreference(
                getString(R.string.friend_notifications));

        setupNotificationStatus();

        mGameNotificationsPreference.setOnPreferenceClickListener(this);
        mFriendNotificationsPreference.setOnPreferenceClickListener(this);

        setNotificationPreferencesVisibility();

        mCachePreference = mPreferenceScreen.findPreference(getString(R.string.clear_cache));
        mCachePreference.setOnPreferenceClickListener(this);

        mLogoutPreference = mPreferenceScreen.findPreference(getString(R.string.log_out_label));
        mLogoutPreference.setOnPreferenceClickListener(this);
        mVersionPreference = mPreferenceScreen.findPreference(getString(R.string.version_label));
        mFeedbackPreference = mPreferenceScreen.findPreference(getString(R.string.give_feedback));
        mFeedbackPreference.setOnPreferenceClickListener(this);

        if (packageInfo != null) {
            mVersionPreference.setSummary(packageInfo.versionName);
        }

        mPresenter.subscribe();
    }

    @Override
    public void updateCacheSummary(String cacheSize) {
        mCachePreference.setSummary(
                String.format(getString(R.string.current_size), cacheSize));
    }

    @Override
    public void clearCacheSuccess() {
        Toast.makeText(SnaptionApplication.getContext(), getString(R.string.clear_cache_success), Toast.LENGTH_LONG).show();
    }

    @Override
    public void clearCacheFailure() {
        Toast.makeText(SnaptionApplication.getContext(), getString(R.string.clear_cache_fail), Toast.LENGTH_LONG).show();
    }

    @Override
    public void updateLoginSummary() {
        if (mPresenter.isLoggedIn()) {
            mLogoutPreference.setTitle(R.string.log_out_label);
            mLogoutPreference.setSummary(String.format(getString(R.string.current_login),
                    mPresenter.getUsername()));
        }
        else {
            mLogoutPreference.setTitle(R.string.log_in_label);
            mLogoutPreference.setSummary("");
        }
    }

    @Override
    public void setPresenter(PreferencesContract.Presenter presenter) {
        mPresenter = presenter;
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
        setupNotificationStatus();
        setNotificationPreferencesVisibility();
        mPresenter.subscribe();
    }

    private void goToLogin() {
        Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
        startActivity(loginIntent);
    }

    private void setNotificationPreferencesVisibility() {
        if (!AuthManager.isLoggedIn()) {
            mPreferenceScreen.removePreference(mNotificationsCategory);
            mPreferenceScreen.removePreference(mGameNotificationsPreference);
            mPreferenceScreen.removePreference(mFriendNotificationsPreference);
        }
        else {
            mPreferenceScreen.addPreference(mNotificationsCategory);
            mPreferenceScreen.addPreference(mGameNotificationsPreference);
            mPreferenceScreen.addPreference(mFriendNotificationsPreference);
        }
    }

    private void setupNotificationStatus() {
        mGameNotificationsPreference.setChecked(AuthManager.isGameNotificationsEnabled());
        mFriendNotificationsPreference.setChecked(AuthManager.isFriendNotificationsEnabled());
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        String key = preference.getKey();

        if (key.equals(getString(R.string.log_out_label))) {
            if (AuthManager.isLoggedIn()) {
                new MaterialDialog.Builder(getActivity())
                        .title(R.string.log_out_label)
                        .content(R.string.log_out_content)
                        .positiveText(R.string.yes)
                        .negativeText(R.string.no)
                        .onPositive((@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) -> {
                            mPresenter.logout();
                            goToLogin();
                        })
                        .show();
            }
            else {
                goToLogin();
            }
        }
        else if (key.equals(getString(R.string.clear_cache))) {
            mPresenter.clearCache();
        }
        else if (key.equals(getString(R.string.give_feedback))) {
            new MaterialDialog.Builder(getActivity())
                    .title(R.string.leaving_title)
                    .content(R.string.leaving_content)
                    .positiveText(R.string.yes)
                    .negativeText(R.string.no)
                    .onPositive((@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) -> {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.feedback_url)));
                        startActivity(browserIntent);
                    })
                    .show();
        }
        else if (key.equals(getString(R.string.game_notifications))) {
            boolean userChoice = !AuthManager.isGameNotificationsEnabled();
            mGameNotificationsPreference.setChecked(userChoice);
            AuthManager.setGameNotificationsEnabled(userChoice);
        }
        else if (key.equals(getString(R.string.friend_notifications))) {
            boolean userChoice = !AuthManager.isFriendNotificationsEnabled();
            mFriendNotificationsPreference.setChecked(userChoice);
            AuthManager.setFriendNotificationsEnabled(userChoice);
        }
        return true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.unsubscribe();
    }
}
