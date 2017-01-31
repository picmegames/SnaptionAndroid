package com.snaptiongame.snaptionapp.data.converters;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.snaptiongame.snaptionapp.data.models.AddFriendRequest;

import java.lang.reflect.Type;

/**
 * Created by nickromero on 1/30/17.
 */

public class AddFriendConverter implements JsonSerializer<AddFriendRequest> {
    @Override
    public JsonElement serialize(AddFriendRequest src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject json = new JsonObject();
        json.addProperty(src.FRIEND_ID, src.mFriendId);
        return json;
    }
}
