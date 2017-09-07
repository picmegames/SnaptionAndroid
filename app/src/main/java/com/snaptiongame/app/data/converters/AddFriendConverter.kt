package com.snaptiongame.app.data.converters

import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.snaptiongame.app.data.models.AddFriendRequest

import java.lang.reflect.Type

/**
 * @author Nick Romero
 */

class AddFriendConverter : JsonSerializer<AddFriendRequest>, JsonDeserializer<AddFriendRequest> {
    override fun serialize(src: AddFriendRequest, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        val json = JsonObject()
        json.addProperty(AddFriendRequest.FRIEND_ID, src.friendId)
        return json
    }

    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): AddFriendRequest? {
        return if (json.isJsonArray) {
            Gson().fromJson<AddFriendRequest>(json.asJsonArray.get(0), typeOfT)
        }
        else Gson().fromJson<AddFriendRequest>(json, typeOfT)

    }
}
