package com.snaptiongame.snaptionapp.data.presentation.view.creategame;

import android.content.Intent;
import android.os.Build;
import android.widget.ImageView;

import com.snaptiongame.snaptionapp.BuildConfig;
import com.snaptiongame.snaptionapp.R;
import com.snaptiongame.snaptionapp.presentation.view.creategame.CreateGameActivity;

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
public class CreateGameActivityTest {

    private CreateGameActivity createGameActivity;

    @Before
    public void setup() {
        createGameActivity = Robolectric.buildActivity(CreateGameActivity.class)
                .create()
                .resume()
                .get();
    }

    @Test
    public void clickingBack_shouldCloseActivity() {
        ShadowActivity shadowActivity = shadowOf(createGameActivity);
        createGameActivity.onBackPressed();
        assertTrue(shadowActivity.isFinishing());
    }

    @Test
    public void clickingImageView_shouldStartImagePicker() {
        ShadowActivity shadowActivity = shadowOf(createGameActivity);
        ImageView imageView = (ImageView) createGameActivity.findViewById(R.id.image);
        imageView.performClick();

        Intent expectedIntent = new Intent(Intent.ACTION_PICK);
        expectedIntent.setType("image/*");
        Intent actualIntent = shadowActivity.getNextStartedActivity();
        assertTrue(actualIntent.filterEquals(expectedIntent));
    }

    @After
    public void tearDown() {
        createGameActivity.finish();
    }
}
