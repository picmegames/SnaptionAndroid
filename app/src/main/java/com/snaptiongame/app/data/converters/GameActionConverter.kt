package com.snaptiongame.app.data.converters

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.snaptiongame.app.data.models.GameAction

import java.lang.reflect.Type

/**
 * @author Tyler Wong
 */

class GameActionConverter : JsonSerializer<GameAction> {

    override fun serialize(src: GameAction, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        val json = JsonObject()
        json.addProperty(src.type, src.targetId)
        json.addProperty(src.choiceType, src.choice)
        return json
    }
}
