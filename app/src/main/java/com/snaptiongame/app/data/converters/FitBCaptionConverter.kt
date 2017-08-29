package com.snaptiongame.app.data.converters

import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import com.snaptiongame.app.data.models.FitBCaption

import java.lang.reflect.Type

/**
 * @author Nick Romero
 */

class FitBCaptionConverter : JsonDeserializer<FitBCaption> {

    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): FitBCaption? {
        return Gson().fromJson<FitBCaption>(json, typeOfT)
    }
}
