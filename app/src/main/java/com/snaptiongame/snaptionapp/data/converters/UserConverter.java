package com.snaptiongame.snaptionapp.data.converters;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.snaptiongame.snaptionapp.data.models.User;

import java.lang.reflect.Type;

/**
 * @author Tyler Wong
 */

public class UserConverter implements JsonSerializer<User>, JsonDeserializer<User> {

    @Override
    public JsonElement serialize(User src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject json = new JsonObject();
        if (src.username == null) {
            json.addProperty(User.PICTURE, src.picture);
            json.addProperty(User.TYPE, src.type);
        }
        else {
            json.addProperty(User.USERNAME, src.username);
        }
        return new Gson().toJsonTree(src);
    }

    @Override
    public User deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (json.isJsonArray()) {
            if (json.getAsJsonArray().size() > 0) {
                return new Gson().fromJson(json.getAsJsonArray().get(0), typeOfT);
            }
            else {
                return new User();
            }
        }
        return new Gson().fromJson(json, typeOfT);
    }
}
