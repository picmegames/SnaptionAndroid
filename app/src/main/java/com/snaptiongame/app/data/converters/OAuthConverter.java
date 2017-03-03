package com.snaptiongame.app.data.converters;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.snaptiongame.app.data.models.OAuthRequest;

import java.lang.reflect.Type;

/**
 * @author Tyler Wong
 */

public class OAuthConverter implements JsonSerializer<OAuthRequest> {

    @Override
    public JsonElement serialize(OAuthRequest src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject json = new JsonObject();
        json.addProperty(OAuthRequest.TOKEN, src.token);
        json.addProperty(OAuthRequest.DEVICE_TOKEN, src.deviceToken);
        json.addProperty(OAuthRequest.DEVICE_TYPE, src.deviceType);
        json.addProperty(OAuthRequest.LINK_TOKEN, src.linkToken);
        return json;
    }
}