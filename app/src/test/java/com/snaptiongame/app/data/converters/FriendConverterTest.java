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
        pal.setIsSnaptionFriend(false);

        jsonObjectSerialize = new JsonObject();
        jsonObjectDeserialize = new JsonObject();

        jsonObjectSerialize.addProperty(Friend.Companion.getID(), pal.getId());
        jsonObjectSerialize.addProperty(Friend.Companion.getFULL_NAME(), pal.getFullName());
        jsonObjectSerialize.addProperty(Friend.Companion.getUSERNAME(), pal.getUsername());
        jsonObjectSerialize.addProperty(Friend.Companion.getPICTURE(), pal.getPicture());
        jsonObjectSerialize.addProperty(Friend.Companion.getIMAGE_WIDTH(), pal.getImageWidth());
        jsonObjectSerialize.addProperty(Friend.Companion.getIMAGE_HEIGHT(), pal.getImageHeight());
        jsonObjectSerialize.addProperty(Friend.Companion.getCOVER(), pal.getCover());
        jsonObjectSerialize.addProperty(Friend.Companion.getEMAIL(), pal.getEmail());
        jsonObjectSerialize.addProperty("isSnaptionFriend", false);

        jsonObjectDeserialize.addProperty(Friend.Companion.getID(), pal.getId());
        jsonObjectDeserialize.addProperty(Friend.Companion.getFULL_NAME(), pal.getFullName());
        jsonObjectDeserialize.addProperty(Friend.Companion.getUSERNAME(), pal.getUsername());
        JsonObject picture = new JsonObject();
        picture.addProperty(Friend.Companion.getIMAGE_URL(), pal.getPicture());
        picture.addProperty(Friend.Companion.getIMAGE_WIDTH(), pal.getImageWidth());
        picture.addProperty(Friend.Companion.getIMAGE_HEIGHT(), pal.getImageHeight());
        jsonObjectDeserialize.add(Friend.Companion.getPICTURE(), picture);
        jsonObjectDeserialize.addProperty(Friend.Companion.getCOVER(), pal.getCover());
        jsonObjectDeserialize.addProperty(Friend.Companion.getEMAIL(), pal.getEmail());
        jsonObjectDeserialize.addProperty("isSnaptionFriend", false);

        converter = new FriendConverter();
    }

    @Test
    public void testSerialize() {
        assertEquals(converter.serialize(pal, Friend.class, null), jsonObjectSerialize);
        assertEquals(converter.deserialize(jsonObjectDeserialize, Friend.class, null), pal);
    }
}
