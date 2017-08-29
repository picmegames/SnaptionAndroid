package com.snaptiongame.app.data.converters

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import com.snaptiongame.app.data.models.Session

import java.lang.reflect.Type

/**
 * @author Tyler Wong
 */

class SessionConverter : JsonDeserializer<Session> {

    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Session {
        return Session(json.asJsonObject.get(Session.USER).asInt)
    }
}
