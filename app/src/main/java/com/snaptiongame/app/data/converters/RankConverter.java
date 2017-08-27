package com.snaptiongame.app.data.converters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.snaptiongame.app.data.models.Rank;

import java.lang.reflect.Type;

/**
 * @author Tyler Wong
 */

public class RankConverter implements JsonDeserializer<Rank> {
    @Override
    public Rank deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Rank rank = new Rank();
        rank.setId(json.getAsJsonObject().get(Rank.ID).getAsInt());
        rank.setTitle(json.getAsJsonObject().get(Rank.TITLE).getAsString());
        return rank;
    }
}
