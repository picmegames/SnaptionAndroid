package com.snaptiongame.app.data.converters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.snaptiongame.app.data.models.ActivityFeedItem;
import com.snaptiongame.app.data.models.Game;
import com.snaptiongame.app.data.models.User;

import java.lang.reflect.Type;

/**
 * @author Tyler Wong
 */

public class ActivityFeedItemConverter implements JsonDeserializer<ActivityFeedItem> {
    @Override
    public ActivityFeedItem deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject activityObject = json.getAsJsonObject();
        User friend = new User();

        if (!activityObject.get(ActivityFeedItem.FRIEND).isJsonNull()) {
            JsonObject friendObject = activityObject.get(ActivityFeedItem.FRIEND).getAsJsonObject();
            friend.username = friendObject.get(User.USERNAME).getAsString();
            friend.fullName = friendObject.get(User.FULL_NAME).getAsString();

            if (!friendObject.get(User.PICTURE).isJsonNull()) {
                JsonObject pictureObject = friendObject.getAsJsonObject(User.PICTURE);
                friend.imageUrl = pictureObject.get(User.IMAGE_URL).getAsString();
            }
        }

        Game game = new Game();

        if (!activityObject.get(ActivityFeedItem.GAME).isJsonNull()) {
            JsonObject gameObject = activityObject.get(ActivityFeedItem.GAME).getAsJsonObject();
            JsonObject picture = gameObject.getAsJsonObject(Game.PICTURE);
            game.imageUrl = picture.get(Game.IMAGE_URL).getAsString();
        }

        return new ActivityFeedItem(activityObject.get(ActivityFeedItem.ID).getAsInt(),
                activityObject.get(ActivityFeedItem.DATE).getAsLong(),
                activityObject.get(ActivityFeedItem.TYPE).getAsInt(),
                null, friend, game, null);
    }
}
