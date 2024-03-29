package com.snaptiongame.app.presentation.view.friends;

import android.os.Build;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;

import com.snaptiongame.app.BuildConfig;
import com.snaptiongame.app.R;
import com.snaptiongame.app.data.models.Friend;
import com.snaptiongame.app.presentation.view.MainActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.robolectric.shadows.support.v4.SupportFragmentTestUtil.startFragment;

/**
 * @author Brian Gouldsberry
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
        fragmentTransaction.commit();
        startFragment(friendsFragment);
    }

    @Test
    public void startingFragment_isNotNull() {
        assertNotNull(friendsFragment);
    }

    @Test
    public void startingFragment_viewsAppear() {
        RecyclerView friends = friendsFragment.getView().findViewById(R.id.friend_list);
        assertNotNull(friends);
    }

    @Test
    public void recyclerView_getsPopulated() {
        //Set up data and make sure the data exists in the recycler views adapter
        RecyclerView friends = friendsFragment.getView().findViewById(R.id.friend_list);
        assertNotNull(friends);
        List<Friend> inst = new ArrayList<>();
        inst.add(new Friend(1, "John Cena", "jcena1234", "", "", ""));
        inst.add(new Friend(2, "Steve Buscemi", "raddude17", "", "", ""));
        inst.add(new Friend(4, "Steve Cenamopolis", "somefakeperson",
                "", "", ""));
        friendsFragment.processFriends(inst);
        FriendsAdapter a  = (FriendsAdapter) friends.getAdapter();
        assertEquals(a.getItemCount(), inst.size());
    }

    @After
    public void tearDown() {
        fragmentTransaction.remove(friendsFragment);
    }
}
