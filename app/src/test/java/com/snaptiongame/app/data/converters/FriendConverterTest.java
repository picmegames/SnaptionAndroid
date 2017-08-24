package com.snaptiongame.app.data.converters;

import com.google.gson.JsonObject;
import com.snaptiongame.app.data.models.Friend;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * @author Brian Gouldsberry
 */

public class FriendConverterTest {
    private Friend pal;
    private JsonObject jsonObjectSerialize;
    private JsonObject jsonObjectDeserialize;
    FriendConverter converter;

    @Before
    public void setup() {
        pal = new Friend(1, "Bill Nye", "sciencedude12", "fakeurl.com",
                "anotherone.com", "j@j.com");
        pal.isSnaptionFriend = false;

        jsonObjectSerialize = new JsonObject();
        jsonObjectDeserialize = new JsonObject();

        jsonObjectSerialize.addProperty(Friend.ID, pal.id);
        jsonObjectSerialize.addProperty(Friend.FULL_NAME, pal.fullName);
        jsonObjectSerialize.addProperty(Friend.USERNAME, pal.username);
        jsonObjectSerialize.addProperty(Friend.PICTURE, pal.picture);
        jsonObjectSerialize.addProperty(Friend.IMAGE_WIDTH, pal.imageWidth);
        jsonObjectSerialize.addProperty(Friend.IMAGE_HEIGHT, pal.imageHeight);
        jsonObjectSerialize.addProperty(Friend.COVER, pal.cover);
        jsonObjectSerialize.addProperty(Friend.EMAIL, pal.email);
        jsonObjectSerialize.addProperty("isSnaptionFriend", false);

        jsonObjectDeserialize.addProperty(Friend.ID, pal.id);
        jsonObjectDeserialize.addProperty(Friend.FULL_NAME, pal.fullName);
        jsonObjectDeserialize.addProperty(Friend.USERNAME, pal.username);
        JsonObject picture = new JsonObject();
        picture.addProperty(Friend.IMAGE_URL, pal.picture);
        picture.addProperty(Friend.IMAGE_WIDTH, pal.imageWidth);
        picture.addProperty(Friend.IMAGE_HEIGHT, pal.imageHeight);
        jsonObjectDeserialize.add(Friend.PICTURE, picture);
        jsonObjectDeserialize.addProperty(Friend.COVER, pal.cover);
        jsonObjectDeserialize.addProperty(Friend.EMAIL, pal.email);
        jsonObjectDeserialize.addProperty("isSnaptionFriend", false);

        converter = new FriendConverter();
    }

    @Test
    public void testSerialize() {
        assertEquals(converter.serialize(pal, Friend.class, null), jsonObjectSerialize);
        assertEquals(converter.deserialize(jsonObjectDeserialize, Friend.class, null), pal);
    }
}
