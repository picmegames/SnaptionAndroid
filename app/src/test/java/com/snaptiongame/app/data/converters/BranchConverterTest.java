package com.snaptiongame.app.data.converters;

import com.google.gson.JsonObject;
import com.snaptiongame.app.data.models.DeepLinkRequest;
import com.snaptiongame.app.data.models.GameInvite;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
/**
 * Created by BrianGouldsberry on 3/6/17.
 */

public class BranchConverterTest {
    private GameInvite invite;
    private DeepLinkRequest req;
    private JsonObject requestObject;
    private JsonObject inviteObject;
    BranchConverter converter;

    @Before
    public void setup() throws JSONException {
        invite = new GameInvite("test", 1);
        req = new DeepLinkRequest(1, "b@gmail.com", "911", "12", "11");

        inviteObject = new JsonObject();
        requestObject = new JsonObject();

        inviteObject.addProperty(GameInvite.INVITE_TOKEN, invite.inviteToken);
        inviteObject.addProperty(GameInvite.GAME_ID, invite.gameId);
        requestObject.addProperty(DeepLinkRequest.GAMEID, req.gameId);
        requestObject.addProperty(DeepLinkRequest.EMAIL, req.email);
        requestObject.addProperty(DeepLinkRequest.FACEBOOKID, req.facebookID);
        requestObject.addProperty(DeepLinkRequest.GOOGLEID, req.googleID);
        requestObject.addProperty(DeepLinkRequest.PHONE, req.phone);


        converter = new BranchConverter();
    }

    @Test
    public void testSerialize() {
        assertEquals(converter.serialize(req, GameInvite.class, null), requestObject);
        assertEquals(BranchConverter.deserializeGameInvite(inviteObject), invite);
    }
}
