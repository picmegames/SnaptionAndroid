package com.snaptiongame.app.data.converters;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.snaptiongame.app.data.models.Caption;
import com.snaptiongame.app.data.models.FitBCaption;
import com.snaptiongame.app.data.models.Snaption;
import com.snaptiongame.app.data.models.Tag;
import com.snaptiongame.app.data.models.User;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Tyler Wong
 */

public class SnaptionConverter implements JsonSerializer<Snaption>, JsonDeserializer<Snaption> {

    @Override
    public JsonElement serialize(Snaption src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject json = new JsonObject();
        json.addProperty(Snaption.USER_ID, src.userId);
        json.addProperty(Snaption.IS_PUBLIC, src.isPublic);
        json.addProperty(Snaption.RATING, src.rating);
        json.addProperty(Snaption.PICTURE, src.picture);
        json.addProperty(Snaption.IMG_TYPE, src.type);

        JsonArray tags = new JsonArray();
        if (src.tags != null && !src.tags.isEmpty()) {
            for (Tag tag : src.tags) {
                tags.add(tag.name);
            }
        }
        json.add(Snaption.TAGS, tags);

        JsonArray friends = new JsonArray();
        if (src.friendIds != null && !src.friendIds.isEmpty()) {
            for (Integer id : src.friendIds) {
                friends.add(id);
            }
        }
        json.add(Snaption.FRIENDS, friends);

        json.add(Snaption.FRIENDS, new Gson().toJsonTree(src.friendIds));
        return json;
    }

    @Override
    public Snaption deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        JsonObject content = json.getAsJsonObject();
        Snaption snaption = new Snaption(
                content.get(Snaption.ID).getAsInt(),
                content.get(Snaption.START_DATE).getAsLong(),
                content.get(Snaption.IS_PUBLIC).getAsBoolean(),
                content.get(Snaption.RATING).getAsInt(),
                content.get(Snaption.PICKER_ID).getAsInt(),
                content.get(Snaption.PICTURE).getAsString(), "");

        JsonElement endDate = content.get(Snaption.END_DATE);
        if (endDate.isJsonNull()) {
            snaption.endDate = 0;
        }
        else {
            snaption.endDate = endDate.getAsLong();
        }

        JsonArray tags = content.getAsJsonArray(Snaption.TAGS);
        List<Tag> gameTags = new ArrayList<>();
        if (tags.size() > 0) {
            for (JsonElement tag : tags) {
                gameTags.add(new Gson().fromJson(tag, Tag.class));
            }
        }
        snaption.tags = gameTags;

        JsonArray users = content.getAsJsonArray(Snaption.USERS);
        List<User> gameUsers = new ArrayList<>();
        if (users.size() > 0) {
            for (JsonElement user : users) {
                gameUsers.add(new Gson().fromJson(user, User.class));
            }
        }
        snaption.users = gameUsers;

        JsonElement topCaption = content.get("topCaption");
        if (topCaption != null && topCaption.isJsonObject()) {
            Caption caption = new Caption();
            caption.assocFitB = new FitBCaption(0, 0,
                    topCaption.getAsJsonObject().get("fitbBefore").getAsString(),
                    topCaption.getAsJsonObject().get("fitbAfter").getAsString(), 0);
            caption.caption = topCaption.getAsJsonObject().get(Caption.CAPTION).getAsString();
            JsonElement picture = topCaption.getAsJsonObject().get(Caption.USER_PICTURE);
            if (!picture.isJsonNull()) {
                caption.creatorPicture = topCaption.getAsJsonObject().get(Caption.USER_PICTURE).getAsString();
            }
            caption.creatorName = topCaption.getAsJsonObject().get(Caption.USER_NAME).getAsString();
            snaption.topCaption = caption;
        }
        return snaption;
    }
}
