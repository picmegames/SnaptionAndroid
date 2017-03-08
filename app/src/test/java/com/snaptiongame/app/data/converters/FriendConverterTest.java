package com.snaptiongame.app.data.converters;

import com.google.gson.JsonObject;
import com.snaptiongame.app.data.models.Friend;

import org.json.JSONException;
import org.json.JSONObject;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by BrianGouldsberry on 3/6/17.
 */

public class FriendConverterTest {
    private Friend pal;
    private JsonObject jsonObject;
    private JSONObject inviteObject;
    FriendConverter converter;

    @Before
    public void setup() throws JSONException {
        pal = new Friend(1, "Bill", "Nye", "Bill Nye", "sciencedude12", "fakeurl.com",
                "anotherone.com", "j@j.com");

        jsonObject = new JsonObject();

        jsonObject.addProperty(Friend.ID, pal.id);
        jsonObject.addProperty(Friend.FIRST, pal.firstName);
        jsonObject.addProperty(Friend.LAST, pal.lastName);
        jsonObject.addProperty(Friend.FULL_NAME, pal.fullName);
        jsonObject.addProperty(Friend.USERNAME, pal.userName);
        jsonObject.addProperty(Friend.PICTURE, pal.picture);
        jsonObject.addProperty(Friend.COVER, pal.cover);
        jsonObject.addProperty(Friend.EMAIL, pal.email);
        jsonObject.addProperty("isSnaptionFriend", false);

        converter = new FriendConverter();
    }

    @Test
    public void testSerialize() {
        assertEquals(converter.serialize(pal, Friend.class, null), jsonObject);
        assertEquals(converter.deserialize(jsonObject, Friend.class, null), pal);
    }
}
