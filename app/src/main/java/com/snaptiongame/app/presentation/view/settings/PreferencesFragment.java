package com.snaptiongame.app.presentation.view.settings;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.snaptiongame.app.R;
import com.snaptiongame.app.SnaptionApplication;
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

    private PreferencesContract.Presenter mPresenter;

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

        mCachePreference = getPreferenceScreen().findPreference(getString(R.string.clear_cache));
        mCachePreference.setOnPreferenceClickListener(this);

        mLogoutPreference = getPreferenceScreen().findPreference(getString(R.string.log_out_label));
        mLogoutPreference.setOnPreferenceClickListener(this);
        mVersionPreference = getPreferenceScreen().findPreference(getString(R.string.version_label));
        mFeedbackPreference = getPreferenceScreen().findPreference(getString(R.string.give_feedback));
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
        mPresenter.subscribe();
    }

    private void goToLogin() {
        Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
        startActivity(loginIntent);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if (preference.getKey().equals(getString(R.string.log_out_label))) {
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
        else if (preference.getKey().equals(getString(R.string.clear_cache))) {
            mPresenter.clearCache();
        }
        else if(preference.getKey().equals(getString(R.string.give_feedback))) {
            new MaterialDialog.Builder(getActivity())
                    .title(R.string.leaving_title)
                    .content(R.string.leaving_content)
                    .positiveText(R.string.yes)
                    .negativeText(R.string.no)
                    .onPositive((@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) -> {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.store_url)));
                        startActivity(browserIntent);
                    })
                    .show();
        }
        return true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.unsubscribe();
    }
}
