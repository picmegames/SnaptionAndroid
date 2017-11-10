package com.snaptiongame.app.data.converters

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.snaptiongame.app.data.models.User

import java.lang.reflect.Type

/**
 * @author Tyler Wong
 */

class UserConverter : JsonSerializer<User>, JsonDeserializer<User> {

    override fun serialize(src: User, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        val json = JsonObject()
        if (src.username == null) {
            json.addProperty(User.PICTURE, src.picture)
            json.addProperty(User.TYPE, src.type)
        }
        else {
            json.addProperty(User.USERNAME, src.username)
        }
        return json
    }

    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): User? {

        val jsonObject: JsonObject
        if (json.isJsonArray) {
            if (json.asJsonArray.size() > 0) {
                jsonObject = json.asJsonArray.get(0).asJsonObject
            }
            else {
                return null
            }
        }
        else {
            jsonObject = json.asJsonObject
        }

        val newUser = User()

        newUser.id = jsonObject.get(User.ID).asInt
        newUser.username = jsonObject.get(User.USERNAME).asString
        newUser.isFriend = jsonObject.get(User.IS_FRIEND).asBoolean
        newUser.exp = jsonObject.get(User.EXP).asInt

        if (!jsonObject.get(User.FULL_NAME).isJsonNull) {
            newUser.fullName = jsonObject.get(User.FULL_NAME).asString
        }

        if (!jsonObject.get(User.PICTURE).isJsonNull) {
            val pictureObject = jsonObject.getAsJsonObject(User.PICTURE)
            newUser.imageUrl = pictureObject.get(User.IMAGE_URL).asString
            newUser.imageWidth = pictureObject.get(User.IMAGE_WIDTH).asInt
            newUser.imageHeight = pictureObject.get(User.IMAGE_HEIGHT).asInt
        }

        return newUser
    }
}
