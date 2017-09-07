package com.snaptiongame.app.data.converters

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import com.snaptiongame.app.data.models.Rank

import java.lang.reflect.Type

/**
 * @author Tyler Wong
 */

class RankConverter : JsonDeserializer<Rank> {
    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Rank {
        val rank = Rank()
        rank.id = json.asJsonObject.get(Rank.ID).asInt
        rank.title = json.asJsonObject.get(Rank.TITLE).asString
        return rank
    }
}
