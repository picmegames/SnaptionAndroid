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
 * @author Brian Gouldsberry
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

        newFriend.setId(object.get(Friend.ID).getAsInt());
        newFriend.setUsername(object.get(Friend.USERNAME).getAsString());

        if (!object.get(Friend.FULL_NAME).isJsonNull()) {
            newFriend.setFullName(object.get(Friend.FULL_NAME).getAsString());
        }

        if (!object.get(User.PICTURE).isJsonNull()) {
            JsonObject pictureObject = object.getAsJsonObject(Friend.PICTURE);
            newFriend.setImageUrl(pictureObject.get(Friend.IMAGE_URL).getAsString());
            newFriend.setImageWidth(pictureObject.get(Friend.IMAGE_WIDTH).getAsInt());
            newFriend.setImageHeight(pictureObject.get(Friend.IMAGE_HEIGHT).getAsInt());
        }
        newFriend.setSnaptionFriend(true);
        return newFriend;
    }
}
