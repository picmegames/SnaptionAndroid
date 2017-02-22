package com.snaptiongame.snaptionapp.data.presentation.view.friends;

import android.content.Intent;
import android.os.Build;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.ImageView;

import com.snaptiongame.snaptionapp.BuildConfig;
import com.snaptiongame.snaptionapp.R;
import com.snaptiongame.snaptionapp.presentation.view.MainActivity;
import com.snaptiongame.snaptionapp.presentation.view.creategame.CreateGameActivity;
import com.snaptiongame.snaptionapp.presentation.view.friends.FriendsFragment;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.robolectric.Shadows.shadowOf;

/**
 * @author Tyler Wong
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
public class FriendsFragmentTest {

    private FriendsFragment friendsFragment;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    @Before
    public void setup() {
        friendsFragment = new FriendsFragment();
        MainActivity activity = Robolectric.buildActivity( MainActivity.class )
                .create()
                .start()
                .resume()
                .get();

        fragmentManager = activity.getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add( friendsFragment, null );
    }

    @Test
    public void startingFragment_isNotNull() {
        assertNotNull(friendsFragment);
    }


    @After
    public void tearDown() {
        fragmentTransaction.remove(friendsFragment);
        fragmentTransaction.commit();
    }
}
