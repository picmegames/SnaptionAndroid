package com.snaptiongame.app.data.converters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.snaptiongame.app.data.models.Session;

import java.lang.reflect.Type;

/**
 * @author Tyler Wong
 */

public class SessionConverter implements JsonDeserializer<Session> {

    @Override
    public Session deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return new Session(json.getAsJsonObject().get(Session.USER).getAsInt());
    }
}
