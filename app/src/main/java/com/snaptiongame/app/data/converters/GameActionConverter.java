package com.snaptiongame.app.data.converters;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.snaptiongame.app.data.models.GameAction;

import java.lang.reflect.Type;

/**
 * @author Tyler Wong
 */

public class GameActionConverter implements JsonSerializer<GameAction> {

    @Override
    public JsonElement serialize(GameAction src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject json = new JsonObject();
        json.addProperty(src.getType(), src.getTargetId());
        json.addProperty(src.getChoiceType(), src.getChoice());
        return json;
    }
}
