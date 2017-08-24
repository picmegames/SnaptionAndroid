package com.snaptiongame.app.data.converters;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.snaptiongame.app.data.models.AddFriendRequest;

import java.lang.reflect.Type;

/**
 * @author Nick Romero
 */

public class AddFriendConverter implements JsonSerializer<AddFriendRequest>,
        JsonDeserializer<AddFriendRequest> {
    @Override
    public JsonElement serialize(AddFriendRequest src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject json = new JsonObject();
        json.addProperty(AddFriendRequest.FRIEND_ID, src.friendId);
        return json;
    }

    @Override
    public AddFriendRequest deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        if (json.isJsonArray()) {
            return new Gson().fromJson(json.getAsJsonArray().get(0), typeOfT);
        }

        return new Gson().fromJson(json, typeOfT);
    }
}
