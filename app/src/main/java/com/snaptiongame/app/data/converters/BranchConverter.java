package com.snaptiongame.app.data.converters;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.snaptiongame.app.data.models.DeepLinkRequest;
import com.snaptiongame.app.data.models.GameInvite;

import org.json.JSONObject;

import java.lang.reflect.Type;

/**
 * Created by BrianGouldsberry on 2/27/17.
 */

public class BranchConverter implements JsonSerializer<DeepLinkRequest> {

    public static GameInvite deserializeGameInvite(JSONObject json) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(String.valueOf(json), GameInvite.class);
    }

    @Override
    public JsonElement serialize(DeepLinkRequest src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject json = new JsonObject();
        json.addProperty(DeepLinkRequest.GAMEID, src.gameId);
        json.addProperty(DeepLinkRequest.PHONE, src.phone);
        json.addProperty(DeepLinkRequest.GOOGLEID, src.googleID);
        json.addProperty(DeepLinkRequest.FACEBOOKID, src.facebookID);
        json.addProperty(DeepLinkRequest.EMAIL, src.email);
        return json;
    }
}
