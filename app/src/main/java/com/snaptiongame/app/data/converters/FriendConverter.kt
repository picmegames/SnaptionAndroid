package com.snaptiongame.app.data.converters

import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.snaptiongame.app.data.models.Friend
import com.snaptiongame.app.data.models.User

import java.lang.reflect.Type

/**
 * @author Brian Gouldsberry
 */

class FriendConverter : JsonSerializer<Friend>, JsonDeserializer<Friend> {

    override fun serialize(src: Friend, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        return Gson().toJsonTree(src)
    }

    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Friend {
        val friendObject = json.asJsonObject
        val newFriend = Friend()

        newFriend.id = friendObject.get(Friend.ID).asInt
        newFriend.username = friendObject.get(Friend.USERNAME).asString

        if (!friendObject.get(Friend.FULL_NAME).isJsonNull) {
            newFriend.fullName = friendObject.get(Friend.FULL_NAME).asString
        }

        if (!friendObject.get(User.PICTURE).isJsonNull) {
            val pictureObject = friendObject.getAsJsonObject(Friend.PICTURE)
            newFriend.imageUrl = pictureObject.get(Friend.IMAGE_URL).asString
            newFriend.imageWidth = pictureObject.get(Friend.IMAGE_WIDTH).asInt
            newFriend.imageHeight = pictureObject.get(Friend.IMAGE_HEIGHT).asInt
        }
        newFriend.isSnaptionFriend = true
        return newFriend
    }
}
