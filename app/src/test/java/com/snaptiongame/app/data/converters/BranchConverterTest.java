package com.snaptiongame.app.data.converters;

import com.google.gson.JsonObject;
import com.snaptiongame.app.data.models.DeepLinkRequest;
import com.snaptiongame.app.data.models.GameInvite;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
/**
 * @author Brian Gouldsberry
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
        requestObject.addProperty(DeepLinkRequest.Companion.getGAME_ID(), req.getGameId());
        requestObject.addProperty(DeepLinkRequest.Companion.getEMAIL(), req.getEmail());
        requestObject.addProperty(DeepLinkRequest.Companion.getFACEBOOK_ID(), req.getFacebookId());
        requestObject.addProperty(DeepLinkRequest.Companion.getGOOGLE_ID(), req.getGoogleId());
        requestObject.addProperty(DeepLinkRequest.Companion.getPHONE(), req.getPhone());


        converter = new BranchConverter();
    }

    @Test
    public void testSerialize() {
        assertEquals(converter.serialize(req, GameInvite.class, null), requestObject);
        assertEquals(BranchConverter.deserializeGameInvite(inviteObject), invite);
    }
}
