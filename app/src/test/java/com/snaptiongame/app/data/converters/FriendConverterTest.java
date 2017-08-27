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
        pal.setSnaptionFriend(false);

        jsonObjectSerialize = new JsonObject();
        jsonObjectDeserialize = new JsonObject();

        jsonObjectSerialize.addProperty(Friend.ID, pal.getId());
        jsonObjectSerialize.addProperty(Friend.FULL_NAME, pal.getFullName());
        jsonObjectSerialize.addProperty(Friend.USERNAME, pal.getUsername());
        jsonObjectSerialize.addProperty(Friend.PICTURE, pal.getPicture());
        jsonObjectSerialize.addProperty(Friend.IMAGE_WIDTH, pal.getImageWidth());
        jsonObjectSerialize.addProperty(Friend.IMAGE_HEIGHT, pal.getImageHeight());
        jsonObjectSerialize.addProperty(Friend.COVER, pal.getCover());
        jsonObjectSerialize.addProperty(Friend.EMAIL, pal.getEmail());
        jsonObjectSerialize.addProperty("isSnaptionFriend", false);

        jsonObjectDeserialize.addProperty(Friend.ID, pal.getId());
        jsonObjectDeserialize.addProperty(Friend.FULL_NAME, pal.getFullName());
        jsonObjectDeserialize.addProperty(Friend.USERNAME, pal.getUsername());
        JsonObject picture = new JsonObject();
        picture.addProperty(Friend.IMAGE_URL, pal.getPicture());
        picture.addProperty(Friend.IMAGE_WIDTH, pal.getImageWidth());
        picture.addProperty(Friend.IMAGE_HEIGHT, pal.getImageHeight());
        jsonObjectDeserialize.add(Friend.PICTURE, picture);
        jsonObjectDeserialize.addProperty(Friend.COVER, pal.getCover());
        jsonObjectDeserialize.addProperty(Friend.EMAIL, pal.getEmail());
        jsonObjectDeserialize.addProperty("isSnaptionFriend", false);

        converter = new FriendConverter();
    }

    @Test
    public void testSerialize() {
        assertEquals(converter.serialize(pal, Friend.class, null), jsonObjectSerialize);
        assertEquals(converter.deserialize(jsonObjectDeserialize, Friend.class, null), pal);
    }
}
