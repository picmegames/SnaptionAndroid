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
        json.addProperty(Game.IS_PUBLIC, src.isPublic());
        json.addProperty(Game.GAME_DURATION, src.getGameDuration());

        if (!src.isFromAnotherGame()) {
            json.addProperty(Game.PICTURE, src.getPicture());
            json.addProperty(Game.IMG_TYPE, src.getType());
        }
        else {
            json.addProperty(Game.GAME_ID, src.getGameId());
        }

        JsonArray tags = new JsonArray();
        if (src.getSendTags() != null && !src.getSendTags().isEmpty()) {
            for (String sendTag : src.getSendTags()) {
                tags.add(sendTag);
            }
        }
        json.add(Game.TAGS, tags);

        JsonArray friends = new JsonArray();
        if (src.getFriendIds() != null && !src.getFriendIds().isEmpty()) {
            for (Integer friendId : src.getFriendIds()) {
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

        game.setNumUpvotes(content.get(Game.NUM_UPVOTES).getAsInt());
        game.setCreatorName(creatorObject.get(User.USERNAME).getAsString());

        if (!creatorObject.get(User.PICTURE).isJsonNull()) {
            game.setCreatorImage(creatorObject.get(User.PICTURE).getAsJsonObject().get(User.IMAGE_URL).getAsString());
        }

        JsonObject picture = content.getAsJsonObject(Game.PICTURE);
        game.setImageUrl(picture.get(Game.IMAGE_URL).getAsString());
        game.setImageWidth(picture.get(Game.IMAGE_WIDTH).getAsInt());
        game.setImageHeight(picture.get(Game.IMAGE_HEIGHT).getAsInt());

        JsonElement endDate = content.get(Game.END_DATE);
        if (endDate.isJsonNull()) {
            game.setEndDate(0);
        }
        else {
            game.setEndDate(endDate.getAsLong());
        }

        JsonArray tags = content.getAsJsonArray(Game.TAGS);
        List<Tag> gameTags = new ArrayList<>();
        if (tags.size() > 0) {
            for (JsonElement tag : tags) {
                gameTags.add(new Gson().fromJson(tag, Tag.class));
            }
        }
        game.setTags(gameTags);

        JsonArray users = content.getAsJsonArray(Game.USERS);
        JsonObject userObject;
        JsonElement pictureObject;
        List<User> gameUsers = new ArrayList<>();
        User newUser;

        if (users.size() > 0) {
            for (JsonElement user : users) {
                newUser = new User();
                userObject = user.getAsJsonObject();
                newUser.setId(userObject.get(User.ID).getAsInt());
                newUser.setUsername(userObject.get(User.USERNAME).getAsString());
                newUser.setFullName(userObject.get(User.FULL_NAME).getAsString());
                pictureObject = userObject.get(User.PICTURE);
                if (!pictureObject.isJsonNull()) {
                    newUser.setImageUrl(pictureObject.getAsJsonObject().get(User.IMAGE_URL).getAsString());
                    newUser.setImageWidth(pictureObject.getAsJsonObject().get(User.IMAGE_WIDTH).getAsInt());
                    newUser.setImageHeight(pictureObject.getAsJsonObject().get(User.IMAGE_HEIGHT).getAsInt());
                }
                gameUsers.add(newUser);
            }
        }
        game.setUsers(gameUsers);

        if (content.get(Game.BEEN_UPVOTED) != null && content.get(Game.BEEN_FLAGGED) != null) {
            game.setBeenUpvoted(content.get(Game.BEEN_UPVOTED).getAsBoolean());
            game.setBeenFlagged(content.get(Game.BEEN_FLAGGED).getAsBoolean());
        }

        JsonElement topCaption = content.get(Game.TOP_CAPTION);

        if (topCaption != null && topCaption.isJsonObject()) {
            Caption caption = new Caption();
            caption.setAssocFitB(new FitBCaption(0, 0,
                    topCaption.getAsJsonObject().get(Game.FITB_BEFORE).getAsString(),
                    topCaption.getAsJsonObject().get(Game.FITB_AFTER).getAsString(), 0));
            caption.setCaption(topCaption.getAsJsonObject().get(Caption.CAPTION).getAsString());
            JsonElement topCaptionerPicture = topCaption.getAsJsonObject().get(Caption.USER_PICTURE);

            if (!topCaptionerPicture.isJsonNull()) {
                caption.setCreatorPicture(topCaptionerPicture.getAsJsonObject().get(User.IMAGE_URL).getAsString());
            }

            caption.setCreatorId(topCaption.getAsJsonObject().get(Caption.USER_ID).getAsInt());
            caption.setCreatorName(topCaption.getAsJsonObject().get(Caption.USERNAME).getAsString());
            game.setTopCaption(caption);
        }
        return game;
    }
}
