package com.snaptiongame.app.data.converters

import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.snaptiongame.app.data.models.CaptionSet

import java.lang.reflect.Type

/**
 * @author Nick Romero
 */

class CaptionSetConverter : JsonSerializer<CaptionSet>, JsonDeserializer<CaptionSet> {

    override fun serialize(src: CaptionSet, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        val json = JsonObject()
        json.addProperty(CaptionSet.TITLE, src.setName)
        json.addProperty(CaptionSet.ID, src.id)
        json.addProperty(CaptionSet.ACTIVE, src.isCaptionSetActive)
        return json
    }

    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): CaptionSet? {
        return Gson().fromJson<CaptionSet>(json, typeOfT)
    }
}