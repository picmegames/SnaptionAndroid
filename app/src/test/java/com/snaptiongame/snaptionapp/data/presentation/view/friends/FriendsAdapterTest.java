package com.snaptiongame.snaptionapp.data.presentation.view.friends;

import com.snaptiongame.snaptionapp.data.models.Friend;
import com.snaptiongame.snaptionapp.presentation.view.friends.FriendsAdapter;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by BrianGouldsberry on 2/9/17.
 */

public class FriendsAdapterTest {

    @Test
    public void clear_isCorrect() throws Exception {
        List<Friend> friends = new ArrayList<>();
        FriendsAdapter inst = new FriendsAdapter(friends);
        assertEquals(inst.getFriends(), friends);

        inst.clearFriends();
        assertEquals(inst.getItemCount(), 0);

        friends.add(new Friend(1, "John", "Cena", "John Cena", "jcena1234", "", "", ""));
        friends.add(new Friend(2, "Steve", "Buscemi", "Steve Buscemi", "raddude17", "", "", ""));
        friends.add(new Friend(4, "Steve", "Cenamopolis", "Steve Cenamopolis", "somefakeperson",
                "", "", ""));
        inst.setFriends(friends);

        assertEquals(inst.getItemCount(), 3);
        assertEquals(inst.getFriends(), friends);

        inst.clearFriends();
        assertEquals(inst.getItemCount(), 0);
    }
}
