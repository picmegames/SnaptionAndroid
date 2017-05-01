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

        newFriend.id = object.get(Friend.ID).getAsInt();
        newFriend.username = object.get(Friend.USERNAME).getAsString();

        if (!object.get(Friend.FULL_NAME).isJsonNull()) {
            newFriend.fullName = object.get(Friend.FULL_NAME).getAsString();
        }

        if (!object.get(User.PICTURE).isJsonNull()) {
            JsonObject pictureObject = object.getAsJsonObject(Friend.PICTURE);
            newFriend.imageUrl = pictureObject.get(Friend.IMAGE_URL).getAsString();
            newFriend.imageWidth = pictureObject.get(Friend.IMAGE_WIDTH).getAsInt();
            newFriend.imageHeight = pictureObject.get(Friend.IMAGE_HEIGHT).getAsInt();
        }
        newFriend.isSnaptionFriend = true;
        return newFriend;
    }
}
