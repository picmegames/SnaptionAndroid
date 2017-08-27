package com.snaptiongame.app.data.converters;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.snaptiongame.app.data.models.DeepLinkRequest;
import com.snaptiongame.app.data.models.GameInvite;

import java.lang.reflect.Type;

/**
 * @author Brian Gouldsberry
 */

public class BranchConverter implements JsonSerializer<DeepLinkRequest> {

    public static GameInvite deserializeGameInvite(JsonElement json) {
        JsonObject content = json.getAsJsonObject();
        String inviteToken = null;
        int gameId = 0;

        if (content.get(GameInvite.INVITE_TOKEN) != null && content.get(GameInvite.GAME_ID) != null) {
            inviteToken = content.get(GameInvite.INVITE_TOKEN).getAsString();
            gameId = content.get(GameInvite.GAME_ID).getAsInt();
        }

        return new GameInvite(inviteToken, gameId);
    }

    @Override
    public JsonElement serialize(DeepLinkRequest src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject json = new JsonObject();
        json.addProperty(DeepLinkRequest.GAME_ID, src.getGameId());
        json.addProperty(DeepLinkRequest.PHONE, src.getPhone());
        json.addProperty(DeepLinkRequest.GOOGLE_ID, src.getGoogleId());
        json.addProperty(DeepLinkRequest.FACEBOOK_ID, src.getFacebookId());
        json.addProperty(DeepLinkRequest.EMAIL, src.getEmail());
        return json;
    }
}
