package com.snaptiongame.snaptionapp.data.converters;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.snaptiongame.snaptionapp.data.models.Like;

import java.lang.reflect.Type;

/**
 * @author Tyler Wong
 */

public class LikeConverter implements JsonSerializer<Like> {

    @Override
    public JsonElement serialize(Like src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject json = new JsonObject();
        json.addProperty(src.type, src.targetId);
        json.addProperty(src.choiceType, src.choice);
        return json;
    }
}
