package com.snaptiongame.snaptionapp.presentation.view.game;

import android.content.Intent;
import android.os.Build;

import com.snaptiongame.snaptionapp.BuildConfig;
import com.snaptiongame.snaptionapp.presentation.view.MainActivity;

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
public class GameActivityTest {

    private GameActivity gameActivity;

    @Before
    public void setup() {
        gameActivity = Robolectric.buildActivity(GameActivity.class)
                .create()
                .resume()
                .get();
    }

    @Test
    public void clickingBack_shouldCloseActivity() {
        ShadowActivity shadowActivity = shadowOf(gameActivity);
        gameActivity.onBackPressed();
        Intent expectedIntent = new Intent(gameActivity, MainActivity.class);
        Intent actualIntent = shadowActivity.getNextStartedActivity();
        assertTrue(actualIntent.filterEquals(expectedIntent));
    }

    @After
    public void tearDown() {
        gameActivity.finish();
    }
}
