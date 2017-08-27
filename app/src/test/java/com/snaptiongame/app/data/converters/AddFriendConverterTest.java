package com.snaptiongame.app.data.converters;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.snaptiongame.app.data.models.AddFriendRequest;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * @author Nick Romero
 */

public class AddFriendConverterTest {
    private AddFriendConverter converter;
    private JsonObject testJson;
    private JsonArray jsonArray;
    private AddFriendRequest friendRequest;


    @Before
    public void setup() {
        converter = new AddFriendConverter();
        testJson = new JsonObject();
        testJson.addProperty(AddFriendRequest.Companion.getFRIEND_ID(), 12345);
        friendRequest = new AddFriendRequest(12345);
        jsonArray = new JsonArray();
        jsonArray.add(testJson);
    }




    @Test
    public void testSerialize() {
        assertEquals(testJson,  converter.serialize(new AddFriendRequest(12345), null, null));
    }

    @Test
    public void testDeserialize() {
       assertTrue(converter.deserialize(jsonArray, AddFriendRequest.class, null) != null);
    }
}
