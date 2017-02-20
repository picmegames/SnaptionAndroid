package com.snaptiongame.snaptionapp.data.models;

import com.snaptiongame.snaptionapp.presentation.view.friends.FriendsFragment;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by BrianGouldsberry on 1/23/17.
 * friends constructor comes in the form of Friend(String id, String first, String last, String fullName, String userName, String picture,
 * String cover, String email)
 */

public class FriendsTest {
    @Test
    public void filter_isCorrect() throws Exception {
        List<Friend> inst = new ArrayList<>(), check;
        assertEquals(FriendsFragment.filterList(inst, "some filter category"), new
                ArrayList<Friend>());
        inst.add(new Friend("1", "John", "Cena", "John Cena", "jcena1234", "", "", ""));
        inst.add(new Friend("2", "Steve", "Buscemi", "Steve Buscemi", "raddude17", "", "", ""));
        inst.add(new Friend("4", "Steve", "Cenamopolis", "Steve Cenamopolis", "somefakeperson",
                "", "", ""));

        check = FriendsFragment.filterList(inst, "Steve");
        assertTrue(check.get(0).fullName.equals("Steve Buscemi"));
        assertTrue(check.get(1).fullName.equals("Steve Cenamopolis"));
        assertEquals(check.size(), 2);

        //check last name match
        check = FriendsFragment.filterList(inst, "cena");
        assertTrue(check.get(0).fullName.equals("John Cena"));
        assertTrue(check.get(1).fullName.equals("Steve Cenamopolis"));
        assertEquals(check.size(), 2);

        //check not in list
        check.clear();
        assertEquals(FriendsFragment.filterList(inst, "zzzz"), new ArrayList<Friend>());

        //check both first and last
        check = FriendsFragment.filterList(inst, "hn ce");
        assertTrue(check.get(0).fullName.equals("John Cena"));
        assertEquals(check.size(), 1);

        //check empty queries
        check = FriendsFragment.filterList(inst, "");
        assertEquals(check, inst);
        check = FriendsFragment.filterList(inst, null);
        assertEquals(check, inst);
    }
}
