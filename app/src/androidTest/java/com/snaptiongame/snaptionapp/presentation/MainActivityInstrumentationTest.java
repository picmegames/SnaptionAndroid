package com.snaptiongame.snaptionapp.presentation;

import android.support.design.widget.NavigationView;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import com.snaptiongame.snaptionapp.R;
import com.snaptiongame.snaptionapp.presentation.view.MainActivity;
import com.snaptiongame.snaptionapp.presentation.view.login.LoginActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;

/**
 * @author Tyler Wong
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityInstrumentationTest {
   @Rule
   public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule(MainActivity.class);

   private MainActivity activity;

   @Before
   public void setup() {
      activity = mActivityRule.getActivity();
   }

   @Test
   public void testClickProfileImageLoginIntent() {
      Intents.init();

      activity.runOnUiThread(() -> {
         NavigationView navigationView = (NavigationView) activity.findViewById(R.id.navigation_view);
         View headerView = navigationView.getHeaderView(0);
         CircleImageView profileImage = (CircleImageView) headerView.findViewById(R.id.profile_image);
         profileImage.performClick();
      });
      intended(hasComponent(LoginActivity.class.getName()));

      Intents.release();
   }
}
