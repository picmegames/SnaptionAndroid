package com.snaptiongame.app.data.converters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.snaptiongame.app.data.models.User;

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
        } else {
            json.addProperty(User.USERNAME, src.username);
        }
        return json;
    }

    @Override
    public User deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        JsonObject object;
        if (json.isJsonArray()) {
            if (json.getAsJsonArray().size() > 0) {
                object = json.getAsJsonArray().get(0).getAsJsonObject();
            }
            else {
                return null;
            }
        }
        else {
            object = json.getAsJsonObject();
        }

        User newUser = new User();

        newUser.id = object.get(User.ID).getAsInt();
        newUser.username = object.get(User.USERNAME).getAsString();
        newUser.isFriend = object.get(User.IS_FRIEND).getAsBoolean();

        if (!object.get(User.FULL_NAME).isJsonNull()) {
            newUser.fullName = object.get(User.FULL_NAME).getAsString();
        }

        if (!object.get(User.PICTURE).isJsonNull()) {
            JsonObject pictureObject = object.getAsJsonObject(User.PICTURE);
            newUser.imageUrl = pictureObject.get(User.IMAGE_URL).getAsString();
            newUser.imageWidth = pictureObject.get(User.IMAGE_WIDTH).getAsInt();
            newUser.imageHeight = pictureObject.get(User.IMAGE_HEIGHT).getAsInt();
        }

        return newUser;
    }
}
