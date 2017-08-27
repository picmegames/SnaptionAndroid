package com.snaptiongame.app.data.converters;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.snaptiongame.app.data.models.ActivityFeedItem;
import com.snaptiongame.app.data.models.Caption;
import com.snaptiongame.app.data.models.Game;
import com.snaptiongame.app.data.models.User;
import com.snaptiongame.app.data.providers.api.ApiProvider;

import java.lang.reflect.Type;

/**
 * @author Tyler Wong
 */

public class ActivityFeedItemConverter implements JsonDeserializer<ActivityFeedItem> {
    @Override
    public ActivityFeedItem deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject activityObject = json.getAsJsonObject();
        Gson gson = ApiProvider.getGson();

        User friend = gson.fromJson(activityObject.get(ActivityFeedItem.FRIEND), User.class);
        Game game = gson.fromJson(activityObject.get(ActivityFeedItem.GAME), Game.class);
        Caption caption = gson.fromJson(activityObject.get(ActivityFeedItem.CAPTION), Caption.class);

        return new ActivityFeedItem(activityObject.get(ActivityFeedItem.ID).getAsInt(),
                activityObject.get(ActivityFeedItem.DATE).getAsLong(),
                activityObject.get(ActivityFeedItem.TYPE).getAsInt(), friend, game, caption);
    }
}
