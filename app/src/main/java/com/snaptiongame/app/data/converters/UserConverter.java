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
        if (src.getUsername() == null) {
            json.addProperty(User.PICTURE, src.getPicture());
            json.addProperty(User.TYPE, src.getType());
        } else {
            json.addProperty(User.USERNAME, src.getUsername());
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

        newUser.setId(object.get(User.ID).getAsInt());
        newUser.setUsername(object.get(User.USERNAME).getAsString());
        newUser.setFriend(object.get(User.IS_FRIEND).getAsBoolean());

        if (!object.get(User.FULL_NAME).isJsonNull()) {
            newUser.setFullName(object.get(User.FULL_NAME).getAsString());
        }

        if (!object.get(User.PICTURE).isJsonNull()) {
            JsonObject pictureObject = object.getAsJsonObject(User.PICTURE);
            newUser.setImageUrl(pictureObject.get(User.IMAGE_URL).getAsString());
            newUser.setImageWidth(pictureObject.get(User.IMAGE_WIDTH).getAsInt());
            newUser.setImageHeight(pictureObject.get(User.IMAGE_HEIGHT).getAsInt());
        }

        return newUser;
    }
}
