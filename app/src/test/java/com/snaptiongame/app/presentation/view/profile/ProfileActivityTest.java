package com.snaptiongame.app.presentation.view.profile;

import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.snaptiongame.app.BuildConfig;
import com.snaptiongame.app.R;

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
 * Created by Brian Gouldsberry on 2/20/17.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
public class ProfileActivityTest {

    private ProfileActivity profileActivity;

    @Before
    public void setup() {
        profileActivity = Robolectric.buildActivity(ProfileActivity.class)
                .create()
                .resume()
                .get();
    }

    @Test
    public void startingActivity_viewsShouldAppear() {
        Toolbar mToolbar = (Toolbar) profileActivity.findViewById(R.id.toolbar);
        assertNotNull(mToolbar);

        ImageView mCoverPhoto = (ImageView) profileActivity.findViewById(R.id.cover_photo);
        assertNotNull(mCoverPhoto);

        ImageView mProfileImg = (ImageView) profileActivity.findViewById(R.id.profile_image);
        assertNotNull(mProfileImg);

        TextView mTitle = (TextView) profileActivity.findViewById(R.id.name);
        assertNotNull(mTitle);

        TextView mMainTitle = (TextView) profileActivity.findViewById(R.id.main_title);
        assertNotNull(mMainTitle);

        CoordinatorLayout mLayout = (CoordinatorLayout) profileActivity.findViewById(R.id.layout);
        assertNotNull(mLayout);

        AppBarLayout mAppBar = (AppBarLayout) profileActivity.findViewById(R.id.app_bar);
        assertNotNull(mAppBar);

        CollapsingToolbarLayout mCollapsingLayout = (CollapsingToolbarLayout) profileActivity
                .findViewById(R.id.collapsing_toolbar);
        assertNotNull(mCollapsingLayout);

        TabLayout mTabLayout = (TabLayout) profileActivity.findViewById(R.id.tab_layout);
        assertNotNull(mTabLayout);

        ViewPager mViewPager = (ViewPager) profileActivity.findViewById(R.id.view_pager);
        assertNotNull(mViewPager);

        FloatingActionButton mFab = (FloatingActionButton) profileActivity.findViewById(R.id.fab);
        assertNotNull(mFab);
    }

    @After
    public void tearDown() {
        profileActivity.finish();
    }
}
