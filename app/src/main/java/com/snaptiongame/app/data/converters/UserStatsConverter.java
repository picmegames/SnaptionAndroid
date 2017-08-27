package com.snaptiongame.app.data.converters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.snaptiongame.app.data.models.UserStats;


import java.lang.reflect.Type;

/**
 * @author Quang Ngo
 */

public class UserStatsConverter implements JsonDeserializer<UserStats> {
    @Override
    public UserStats deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();
        UserStats newStats = new UserStats();

        newStats.setRank(obj.get(UserStats.RANK).getAsString());
        newStats.setCaptionsCreated(obj.get(UserStats.CAPTIONS_CREATED).getAsInt());
        newStats.setCaptionUpvotes(obj.get(UserStats.CAPTION_UPVOTES).getAsInt());
        newStats.setGamesCreated(obj.get(UserStats.GAMES_CREATED).getAsInt());
        newStats.setHighestGameUpvote(obj.get(UserStats.HIGHEST_GAME_UPVOTE).getAsInt());
        newStats.setTopCaptionCount(obj.get(UserStats.TOP_CAPTION_COUNT).getAsInt());
        newStats.setExp(obj.get(UserStats.EXP).getAsInt());

        return newStats;
    }
}
