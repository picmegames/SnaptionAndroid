package com.snaptiongame.snaptionapp.data.presentation.view;

import android.content.Intent;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.view.View;

import com.snaptiongame.snaptionapp.BuildConfig;
import com.snaptiongame.snaptionapp.R;
import com.snaptiongame.snaptionapp.presentation.view.MainActivity;
import com.snaptiongame.snaptionapp.presentation.view.login.LoginActivity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;

import static junit.framework.Assert.assertTrue;

/**
 * @author Tyler Wong
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
public class MainActivityTest {

    @Test
    public void clickingHeaderView_shouldStartLoginActivity() {
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        NavigationView navigationView = (NavigationView) mainActivity.findViewById(R.id.navigation_view);
        View headerView = navigationView.getHeaderView(0);
        headerView.performClick();

        Intent expectedIntent = new Intent(mainActivity, LoginActivity.class);
        ShadowActivity shadowActivity = Shadows.shadowOf(mainActivity);
        Intent actualIntent = shadowActivity.getNextStartedActivity();
        assertTrue(actualIntent.filterEquals(expectedIntent));
    }

    @Test
    public void clickingFab_shouldStartLoginActivity() {
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        FloatingActionButton fab = (FloatingActionButton) mainActivity.findViewById(R.id.fab);
        fab.performClick();

        Intent expectedIntent = new Intent(mainActivity, LoginActivity.class);
        ShadowActivity shadowActivity = Shadows.shadowOf(mainActivity);
        Intent actualIntent = shadowActivity.getNextStartedActivity();
        assertTrue(actualIntent.filterEquals(expectedIntent));
    }
}
