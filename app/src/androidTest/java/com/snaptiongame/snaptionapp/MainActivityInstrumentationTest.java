package com.snaptiongame.snaptionapp;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.snaptiongame.snaptionapp.presentation.view.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * @author Tyler Wong
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityInstrumentationTest {
   @Rule
   public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
         MainActivity.class);

   @Test
   public void click_sameActivity() {
      onView(withId(R.id.wall))
            .perform(click())
            .check(matches(isDisplayed()));
   }
}
