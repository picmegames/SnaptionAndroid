package com.snaptiongame.app.data.converters

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.snaptiongame.app.data.models.DeepLinkRequest
import com.snaptiongame.app.data.models.GameInvite

import java.lang.reflect.Type

/**
 * @author Brian Gouldsberry
 */

class BranchConverter : JsonSerializer<DeepLinkRequest> {

    override fun serialize(src: DeepLinkRequest, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        val json = JsonObject()
        json.addProperty(DeepLinkRequest.GAME_ID, src.gameId)
        json.addProperty(DeepLinkRequest.PHONE, src.phone)
        json.addProperty(DeepLinkRequest.GOOGLE_ID, src.googleId)
        json.addProperty(DeepLinkRequest.FACEBOOK_ID, src.facebookId)
        json.addProperty(DeepLinkRequest.EMAIL, src.email)
        return json
    }

    companion object {
        fun deserializeGameInvite(json: JsonElement): GameInvite {
            val content = json.asJsonObject
            var inviteToken: String? = null
            var gameId = 0

            if (content.get(GameInvite.INVITE_TOKEN) != null && content.get(GameInvite.GAME_ID) != null) {
                inviteToken = content.get(GameInvite.INVITE_TOKEN).asString
                gameId = content.get(GameInvite.GAME_ID).asInt
            }

            return GameInvite(inviteToken, gameId)
        }
    }
}
