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
        json.addProperty(Game.IS_PUBLIC, src.isPublic);
        json.addProperty(Game.GAME_DURATION, src.gameDuration);

        if (!src.isFromAnotherGame) {
            json.addProperty(Game.PICTURE, src.picture);
            json.addProperty(Game.IMG_TYPE, src.type);
        }
        else {
            json.addProperty(Game.GAME_ID, src.gameId);
        }

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
        return json;
    }

    @Override
    public Game deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        JsonObject content = json.getAsJsonObject();
        JsonObject creatorObject = content.get(Game.PICKER).getAsJsonObject();

        Game game = new Game(
                content.get(Game.ID).getAsInt(),
                content.get(Game.START_DATE).getAsLong(),
                content.get(Game.IS_PUBLIC).getAsBoolean(),
                creatorObject.get(Game.ID).getAsInt(), "", "");

        game.numUpvotes = content.get(Game.NUM_UPVOTES).getAsInt();
        game.creatorName = creatorObject.get(User.USERNAME).getAsString();

        if (!creatorObject.get(User.PICTURE).isJsonNull()) {
            game.creatorImage = creatorObject.get(User.PICTURE).getAsJsonObject().get(User.IMAGE_URL).getAsString();
        }

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
        JsonElement pictureObject;
        List<User> gameUsers = new ArrayList<>();
        User newUser;

        if (users.size() > 0) {
            for (JsonElement user : users) {
                newUser = new User();
                userObject = user.getAsJsonObject();
                newUser.id = userObject.get(User.ID).getAsInt();
                newUser.username = userObject.get(User.USERNAME).getAsString();
                newUser.fullName = userObject.get(User.FULL_NAME).getAsString();
                newUser.exp = userObject.get(User.EXP).getAsInt();
                pictureObject = userObject.get(User.PICTURE);
                if (!pictureObject.isJsonNull()) {
                    newUser.imageUrl = pictureObject.getAsJsonObject().get(User.IMAGE_URL).getAsString();
                    newUser.imageWidth = pictureObject.getAsJsonObject().get(User.IMAGE_WIDTH).getAsInt();
                    newUser.imageHeight = pictureObject.getAsJsonObject().get(User.IMAGE_HEIGHT).getAsInt();
                }
                gameUsers.add(newUser);
            }
        }
        game.users = gameUsers;

        if (content.get(Game.BEEN_UPVOTED) != null && content.get(Game.BEEN_FLAGGED) != null) {
            game.beenUpvoted = content.get(Game.BEEN_UPVOTED).getAsBoolean();
            game.beenFlagged = content.get(Game.BEEN_FLAGGED).getAsBoolean();
        }

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

            caption.creatorId = topCaption.getAsJsonObject().get(Caption.USER_ID).getAsInt();
            caption.creatorName = topCaption.getAsJsonObject().get(Caption.USERNAME).getAsString();
            game.topCaption = caption;
        }
        return game;
    }
}
