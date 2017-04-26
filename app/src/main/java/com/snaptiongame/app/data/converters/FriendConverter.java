package com.snaptiongame.app.data.converters;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.snaptiongame.app.data.models.Friend;
import com.snaptiongame.app.data.models.User;

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
        JsonObject object = json.getAsJsonObject();
        Friend newFriend = new Friend();

        newFriend.id = object.get(User.ID).getAsInt();
        newFriend.username = object.get(User.USERNAME).getAsString();

        if (!object.get(User.PICTURE).isJsonNull()) {
            JsonObject pictureObject = object.getAsJsonObject(User.PICTURE);
            newFriend.imageUrl = pictureObject.get(User.IMAGE_URL).getAsString();
            newFriend.imageWidth = pictureObject.get(User.IMAGE_WIDTH).getAsInt();
            newFriend.imageHeight = pictureObject.get(User.IMAGE_HEIGHT).getAsInt();
        }
        return newFriend;
    }
}
