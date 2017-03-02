package com.snaptiongame.app.data.converters;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.snaptiongame.app.data.models.Friend;

import java.lang.reflect.Type;

/**
 * Created by BrianGouldsberry on 1/29/17.
 */

public class FriendConverter implements JsonSerializer<Friend>, JsonDeserializer<Friend> {

    @Override
    public JsonElement serialize(Friend src, Type typeOfSrc, JsonSerializationContext context) {
        return new Gson().toJsonTree(src);
    }

    @Override
    public Friend deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        if (json.isJsonArray()) {
            return new Gson().fromJson(json.getAsJsonArray().get(0), typeOfT);
        }
        return new Gson().fromJson(json, typeOfT);
    }
}
