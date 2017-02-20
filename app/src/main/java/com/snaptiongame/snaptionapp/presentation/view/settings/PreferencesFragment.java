package com.snaptiongame.snaptionapp.presentation.view.settings;

import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.view.View;
import android.widget.ListView;

import com.snaptiongame.snaptionapp.R;

import timber.log.Timber;

/**
 * @author Tyler Wong
 */
public class PreferencesFragment extends PreferenceFragment implements
        Preference.OnPreferenceClickListener {
    private Preference mVersionPreference;

    private SharedPreferences mPref;
    private boolean mListStyled = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        if (packageInfo != null) {
            mVersionPreference.setSummary(packageInfo.versionName);
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

    @Override
    public boolean onPreferenceClick(Preference preference) {
        return true;
    }
}
