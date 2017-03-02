package com.snaptiongame.app.data.converters;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.snaptiongame.app.data.models.FitBCaption;

import java.lang.reflect.Type;

/**
 * Created by nickromero on 2/12/17.
 */

public class FitBCaptionConverter implements JsonDeserializer<FitBCaption> {

    @Override
    public FitBCaption deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        return new Gson().fromJson(json, typeOfT);
    }
}
