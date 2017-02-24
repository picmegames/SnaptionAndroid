package com.snaptiongame.snaptionapp.presentation.view.settings;

import android.os.Build;

import com.snaptiongame.snaptionapp.BuildConfig;

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
public class PreferencesActivityTest {

    private PreferencesActivity preferencesActivity;

    @Before
    public void setup() {
        preferencesActivity = Robolectric.buildActivity(PreferencesActivity.class)
                .create()
                .resume()
                .get();
    }

    @Test
    public void clickingBack_shouldCloseActivity() {
        ShadowActivity shadowActivity = shadowOf(preferencesActivity);
        preferencesActivity.onBackPressed();
        assertTrue(shadowActivity.isFinishing());
    }

    @After
    public void tearDown() {
        preferencesActivity.finish();
    }
}
