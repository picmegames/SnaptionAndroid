package com.snaptiongame.app.presentation.view.settings;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
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
    private Preference cachePreference;
    private Preference logoutPreference;
    private Preference versionPreference;
    private Preference licensesPreference;
    private Preference feedbackPreference;
    private PreferenceCategory notificationsCategory;
    private SwitchPreference gameNotificationsPreference;
    private SwitchPreference friendNotificationsPreference;

    private PreferencesContract.Presenter presenter;
    private PreferenceScreen preferenceScreen;
    private MaterialDialog licensesDialog;
    private MaterialDialog feedbackDialog;
    private WebView licensesWebView;
    private WebView feedbackWebView;
    private ImageView puffinLogo;

    private boolean listStyled = false;
    private final EasterEgg puffinEasterEgg = new EasterEgg();

    public static PreferencesFragment newInstance() {
        return new PreferencesFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new PreferencesPresenter(this);

        View view = getView();
        ListView list = null;
        if (view != null) {
            list = view.findViewById(android.R.id.list);
            puffinLogo = view.getRootView().findViewById(R.id.puffinlogo);
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

        preferenceScreen = getPreferenceScreen();

        notificationsCategory = (PreferenceCategory) preferenceScreen.findPreference(
                getString(R.string.notifications));
        gameNotificationsPreference = (SwitchPreference) preferenceScreen.findPreference(
                getString(R.string.game_notifications));
        friendNotificationsPreference = (SwitchPreference) preferenceScreen.findPreference(
                getString(R.string.friend_notifications));

        setupNotificationStatus();

        gameNotificationsPreference.setOnPreferenceClickListener(this);
        friendNotificationsPreference.setOnPreferenceClickListener(this);

        setNotificationPreferencesVisibility();

        cachePreference = preferenceScreen.findPreference(getString(R.string.clear_cache));
        cachePreference.setOnPreferenceClickListener(this);

        logoutPreference = preferenceScreen.findPreference(getString(R.string.log_out_label));
        logoutPreference.setOnPreferenceClickListener(this);
        versionPreference = preferenceScreen.findPreference(getString(R.string.version_label));
        versionPreference.setOnPreferenceClickListener(this);
        licensesPreference = preferenceScreen.findPreference(getString(R.string.licenses));
        licensesPreference.setOnPreferenceClickListener(this);
        feedbackPreference = preferenceScreen.findPreference(getString(R.string.give_feedback));
        feedbackPreference.setOnPreferenceClickListener(this);

        if (packageInfo != null) {
            versionPreference.setSummary(packageInfo.versionName);
        }
        licensesWebView = new WebView(getActivity());
        feedbackWebView = new WebView(getActivity());
        feedbackWebView.getSettings().setJavaScriptEnabled(true);

        presenter.subscribe();
    }

    @Override
    public void updateCacheSummary(String cacheSize) {
        cachePreference.setSummary(
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
        if (presenter.isLoggedIn()) {
            logoutPreference.setTitle(R.string.log_out_label);
            logoutPreference.setSummary(String.format(getString(R.string.current_login),
                    presenter.getUsername()));
        }
        else {
            logoutPreference.setTitle(R.string.log_in_label);
            logoutPreference.setSummary("");
        }
    }

    @Override
    public void setPresenter(PreferencesContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!listStyled) {
            View view = getView();
            if (view != null) {
                ListView list = view.findViewById(android.R.id.list);
                list.setPadding(0, 0, 0, 0);
                list.setDivider(ContextCompat.getDrawable(SnaptionApplication.getContext(), R.drawable.line_divider));
                listStyled = true;
                puffinLogo = view.getRootView().findViewById(R.id.puffinlogo);
            }
        }
        setupNotificationStatus();
        setNotificationPreferencesVisibility();
        presenter.subscribe();
    }

    private void goToLogin() {
        Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
        startActivity(loginIntent);
    }

    private void setNotificationPreferencesVisibility() {
        if (!AuthManager.isLoggedIn()) {
            preferenceScreen.removePreference(notificationsCategory);
            preferenceScreen.removePreference(gameNotificationsPreference);
            preferenceScreen.removePreference(friendNotificationsPreference);
        }
        else {
            preferenceScreen.addPreference(notificationsCategory);
            preferenceScreen.addPreference(gameNotificationsPreference);
            preferenceScreen.addPreference(friendNotificationsPreference);
        }
    }

    private void setupNotificationStatus() {
        gameNotificationsPreference.setChecked(AuthManager.isGameNotificationsEnabled());
        friendNotificationsPreference.setChecked(AuthManager.isFriendNotificationsEnabled());
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
                            presenter.logout();
                            goToLogin();
                        })
                        .show();
            }
            else {
                goToLogin();
            }
        }
        else if (key.equals(getString(R.string.version_label))) {
           puffinEasterEgg.update(puffinLogo);
        }
        else if (key.equals(getString(R.string.clear_cache))) {
            presenter.clearCache();
        }
        else if (key.equals(getString(R.string.licenses))) {
            licensesWebView.loadUrl(getString(R.string.licenses_url));
            if (licensesDialog == null) {
                licensesDialog = new MaterialDialog.Builder(getActivity())
                        .title(R.string.licenses)
                        .customView(licensesWebView, false)
                        .positiveText(R.string.close)
                        .show();
            }
            else {
                licensesDialog.show();
            }
        }
        else if (key.equals(getString(R.string.give_feedback))) {
            feedbackWebView.reload();
            feedbackWebView.loadUrl(getString(R.string.feedback_url));
            if (feedbackDialog == null) {
                feedbackDialog = new MaterialDialog.Builder(getActivity())
                        .title(R.string.give_feedback)
                        .customView(feedbackWebView, false)
                        .positiveText(R.string.close)
                        .show();
            }
            else {
                feedbackDialog.show();
            }
        }
        else if (key.equals(getString(R.string.game_notifications))) {
            boolean userChoice = !AuthManager.isGameNotificationsEnabled();
            gameNotificationsPreference.setChecked(userChoice);
            AuthManager.setGameNotificationsEnabled(userChoice);
        }
        else if (key.equals(getString(R.string.friend_notifications))) {
            boolean userChoice = !AuthManager.isFriendNotificationsEnabled();
            friendNotificationsPreference.setChecked(userChoice);
            AuthManager.setFriendNotificationsEnabled(userChoice);
        }
        return true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.unsubscribe();
    }
}
