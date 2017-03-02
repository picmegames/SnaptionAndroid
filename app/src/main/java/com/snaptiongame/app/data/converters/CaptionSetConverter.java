package com.snaptiongame.app.data.converters;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.snaptiongame.app.data.models.CaptionSet;

import java.lang.reflect.Type;

/**
 * Created by nickromero on 2/12/17.
 */

public class CaptionSetConverter implements JsonSerializer<CaptionSet>, JsonDeserializer<CaptionSet> {

    @Override
    public JsonElement serialize(CaptionSet src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject json = new JsonObject();
        json.addProperty(CaptionSet.TITLE, src.captionSetTitle);
        json.addProperty(CaptionSet.ID, src.id);
        json.addProperty(CaptionSet.ACTIVE, src.isCaptionSetActive);
        return json;
    }

    @Override
    public CaptionSet deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        return new Gson().fromJson(json, typeOfT);
    }
}