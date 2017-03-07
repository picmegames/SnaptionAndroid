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
import com.snaptiongame.app.data.models.Game;
import com.snaptiongame.app.data.models.Tag;
import com.snaptiongame.app.data.models.User;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Tyler Wong
 */

public class GameConverter implements JsonSerializer<Game>, JsonDeserializer<Game> {

    @Override
    public JsonElement serialize(Game src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject json = new JsonObject();
        json.addProperty(Game.USER_ID, src.userId);
        json.addProperty(Game.IS_PUBLIC, src.isPublic);
        json.addProperty(Game.RATING, src.rating);
        json.addProperty(Game.PICTURE, src.picture);
        json.addProperty(Game.IMG_TYPE, src.type);

        JsonArray tags = new JsonArray();
        if (src.tags != null && !src.tags.isEmpty()) {
            for (Tag tag : src.tags) {
                tags.add(tag.name);
            }
        }
        json.add(Game.TAGS, tags);

        JsonArray friends = new JsonArray();
        if (src.friendIds != null && !src.friendIds.isEmpty()) {
            for (Integer id : src.friendIds) {
                friends.add(id);
            }
        }
        json.add(Game.FRIENDS, friends);

        json.add(Game.FRIENDS, new Gson().toJsonTree(src.friendIds));
        return json;
    }

    @Override
    public Game deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        JsonObject content = json.getAsJsonObject();
        Game game = new Game(
                content.get(Game.ID).getAsInt(),
                content.get(Game.START_DATE).getAsLong(),
                content.get(Game.IS_PUBLIC).getAsBoolean(),
                content.get(Game.RATING).getAsInt(),
                content.get(Game.PICKER_ID).getAsInt(),
                content.get(Game.PICTURE).getAsString(), "");

        JsonElement endDate = content.get(Game.END_DATE);
        if (endDate.isJsonNull()) {
            game.endDate = 0;
        }
        else {
            game.endDate = endDate.getAsLong();
        }

        JsonArray tags = content.getAsJsonArray(Game.TAGS);
        List<Tag> gameTags = new ArrayList<>();
        if (tags.size() > 0) {
            for (JsonElement tag : tags) {
                gameTags.add(new Gson().fromJson(tag, Tag.class));
            }
        }
        game.tags = gameTags;

        JsonArray users = content.getAsJsonArray(Game.USERS);
        List<User> gameUsers = new ArrayList<>();
        if (users.size() > 0) {
            for (JsonElement user : users) {
                gameUsers.add(new Gson().fromJson(user, User.class));
            }
        }
        game.users = gameUsers;

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
            game.topCaption = caption;
        }
        return game;
    }
}
