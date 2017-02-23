package com.snaptiongame.snaptionapp.data.presentation.view.friends;

import android.content.Intent;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.snaptiongame.snaptionapp.BuildConfig;
import com.snaptiongame.snaptionapp.R;
import com.snaptiongame.snaptionapp.data.models.Friend;
import com.snaptiongame.snaptionapp.presentation.view.MainActivity;
import com.snaptiongame.snaptionapp.presentation.view.creategame.CreateGameActivity;
import com.snaptiongame.snaptionapp.presentation.view.friends.FriendsAdapter;
import com.snaptiongame.snaptionapp.presentation.view.friends.FriendsFragment;

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
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.robolectric.Shadows.shadowOf;
import static org.robolectric.shadows.support.v4.SupportFragmentTestUtil.startFragment;

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
        fragmentTransaction.commit();
        startFragment(friendsFragment);
    }

    @Test
    public void startingFragment_isNotNull() {
        assertNotNull(friendsFragment);
    }

    @Test
    public void startingFragment_viewsAppear() {
        RecyclerView friends = (RecyclerView) friendsFragment.getView().findViewById(R.id.friend_list);
        assertNotNull(friends);
        EditText query = (EditText) friendsFragment.getView().findViewById(R.id.query_field);
        assertNotNull(query);
        Button clear = (Button) friendsFragment.getView().findViewById(R.id.clear_button);
        assertNotNull(clear);
    }

    @Test
    public void recyclerView_getsPopulated() {
        //Set up data and make sure the data exists in the recycler views adapter
        RecyclerView friends = (RecyclerView) friendsFragment.getView().findViewById(R.id.friend_list);
        assertNotNull(friends);
        List<Friend> inst = new ArrayList<>();
        inst.add(new Friend("1", "John", "Cena", "John Cena", "jcena1234", "", "", ""));
        inst.add(new Friend("2", "Steve", "Buscemi", "Steve Buscemi", "raddude17", "", "", ""));
        inst.add(new Friend("4", "Steve", "Cenamopolis", "Steve Cenamopolis", "somefakeperson",
                "", "", ""));
        friendsFragment.processFriends(inst);
        FriendsAdapter a  = (FriendsAdapter) friends.getAdapter();
        assertEquals(a.getItemCount(), inst.size());
    }

    public void filterList_isCorrect() {

    }


    @After
    public void tearDown() {
        fragmentTransaction.remove(friendsFragment);
    }
}
