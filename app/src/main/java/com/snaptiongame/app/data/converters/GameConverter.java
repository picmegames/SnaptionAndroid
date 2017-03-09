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
        if (src.sendTags != null && !src.sendTags.isEmpty()) {
            for (String sendTag : src.sendTags) {
                tags.add(sendTag);
            }
        }
        json.add(Game.TAGS, tags);

        JsonArray friends = new JsonArray();
        if (src.friendIds != null && !src.friendIds.isEmpty()) {
            for (Integer friendId : src.friendIds) {
                friends.add(friendId);
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
                content.get(Game.PICKER_ID).getAsInt(), "", "");

        JsonObject picture = content.getAsJsonObject(Game.PICTURE);
        game.imageUrl = picture.get(Game.IMAGE_URL).getAsString();
        game.imageWidth = picture.get(Game.IMAGE_WIDTH).getAsInt();
        game.imageHeight = picture.get(Game.IMAGE_HEIGHT).getAsInt();

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
        JsonObject userObject;
        JsonObject pictureObject;
        List<User> gameUsers = new ArrayList<>();
        User newUser;

        if (users.size() > 0) {
            for (JsonElement user : users) {
                newUser = new User();
                userObject = user.getAsJsonObject();
                newUser.id = userObject.get(User.ID).getAsInt();
                newUser.username = userObject.get(User.USERNAME).getAsString();
                newUser.exp = userObject.get(User.EXP).getAsInt();
                pictureObject = userObject.getAsJsonObject(User.PICTURE);
                newUser.imageUrl = pictureObject.get(User.IMAGE_URL).getAsString();
                newUser.imageWidth = pictureObject.get(User.IMAGE_WIDTH).getAsInt();
                newUser.imageHeight = pictureObject.get(User.IMAGE_HEIGHT).getAsInt();
                newUser.rankId = userObject.get(User.RANK_ID).getAsInt();
                gameUsers.add(newUser);
            }
        }
        game.users = gameUsers;

        JsonElement topCaption = content.get(Game.TOP_CAPTION);

        if (topCaption != null && topCaption.isJsonObject()) {
            Caption caption = new Caption();
            caption.assocFitB = new FitBCaption(0, 0,
                    topCaption.getAsJsonObject().get(Game.FITB_BEFORE).getAsString(),
                    topCaption.getAsJsonObject().get(Game.FITB_AFTER).getAsString(), 0);
            caption.caption = topCaption.getAsJsonObject().get(Caption.CAPTION).getAsString();
            JsonElement topCaptionerPicture = topCaption.getAsJsonObject().get(Caption.USER_PICTURE);

            if (!topCaptionerPicture.isJsonNull()) {
                caption.creatorPicture = topCaptionerPicture.getAsJsonObject().get(User.IMAGE_URL).getAsString();
            }

            caption.creatorName = topCaption.getAsJsonObject().get(Caption.USERNAME).getAsString();
            game.topCaption = caption;
        }
        return game;
    }
}
