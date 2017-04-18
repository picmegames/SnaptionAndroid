package com.snaptiongame.app.presentation.view.main;

import android.content.Intent;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.view.View;

import com.snaptiongame.app.BuildConfig;
import com.snaptiongame.app.R;
import com.snaptiongame.app.presentation.view.login.LoginActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;

import static junit.framework.Assert.assertTrue;
import static org.robolectric.Shadows.shadowOf;

/**
 * @author Tyler Wong
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
public class MainActivityTest {

    private MainActivity mainActivity;

    @Before
    public void setup() {
        mainActivity = Robolectric.buildActivity(MainActivity.class)
                .create()
                .resume()
                .get();
    }

//    @Test
//    public void clickingBack_shouldDoNothing() {
//        ShadowActivity shadowActivity = shadowOf(mainActivity);
//        mainActivity.onBackPressed();
//        assertTrue(shadowActivity.isFinishing());
//    }

    @Test
    public void clickingHeaderView_shouldStartLoginActivity() {
        ShadowActivity shadowActivity = shadowOf(mainActivity);
        NavigationView navigationView = (NavigationView) mainActivity.findViewById(R.id.navigation_view);
        View headerView = navigationView.getHeaderView(0);
        headerView.performClick();

        Intent expectedIntent = new Intent(mainActivity, LoginActivity.class);
        Intent actualIntent = shadowActivity.getNextStartedActivity();
        assertTrue(actualIntent.filterEquals(expectedIntent));
    }

    @Test
    public void clickingFab_shouldStartLoginActivity() {
        ShadowActivity shadowActivity = shadowOf(mainActivity);
        FloatingActionButton fab = (FloatingActionButton) mainActivity.findViewById(R.id.fab);
        fab.performClick();

        Intent expectedIntent = new Intent(mainActivity, LoginActivity.class);
        Intent actualIntent = shadowActivity.getNextStartedActivity();
        assertTrue(actualIntent.filterEquals(expectedIntent));
    }

    @After
    public void tearDown() {
        mainActivity.finish();
    }
}
