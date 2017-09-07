package com.snaptiongame.app.data.converters

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import com.snaptiongame.app.data.models.UserStats


import java.lang.reflect.Type

/**
 * @author Quang Ngo
 */

class UserStatsConverter : JsonDeserializer<UserStats> {
    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): UserStats {
        val obj = json.asJsonObject
        val newStats = UserStats()

        newStats.rank = obj.get(UserStats.RANK).asString
        newStats.captionsCreated = obj.get(UserStats.CAPTIONS_CREATED).asInt
        newStats.captionUpvotes = obj.get(UserStats.CAPTION_UPVOTES).asInt
        newStats.gamesCreated = obj.get(UserStats.GAMES_CREATED).asInt
        newStats.highestGameUpvote = obj.get(UserStats.HIGHEST_GAME_UPVOTE).asInt
        newStats.topCaptionCount = obj.get(UserStats.TOP_CAPTION_COUNT).asInt
        newStats.exp = obj.get(UserStats.EXP).asInt

        return newStats
    }
}
