package com.snaptiongame.app.data.converters


import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.snaptiongame.app.data.models.Caption
import com.snaptiongame.app.data.models.FitBCaption
import com.snaptiongame.app.data.models.User

import java.lang.reflect.Type

/**
 * @author Tyler Wong
 */

class CaptionConverter : JsonSerializer<Caption>, JsonDeserializer<Caption> {

    override fun serialize(src: Caption, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        val json = JsonObject()
        json.addProperty(Caption.FITB_ID_SEND, src.fitbIdSend)
        json.addProperty(Caption.CAPTION, src.caption)
        return json
    }

    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Caption {
        val content = json.asJsonObject
        val caption = Gson().fromJson<Caption>(json, typeOfT)
        val creator = content.getAsJsonObject(Caption.CREATOR)

        if (creator.isJsonObject) {
            caption.creatorName = creator.get(Caption.USERNAME).asString
            val picture = creator.get(User.PICTURE).asJsonObject
            if (!picture.isJsonNull) {
                caption.creatorPicture = picture.get(User.IMAGE_URL).asString
            }
        }

        caption.creatorId = creator.get(User.ID).asInt
        val fitBCaption = content.get(Caption.FITB_OTHER)

        if (fitBCaption.isJsonObject) {
            caption.assocFitB = Gson().fromJson(fitBCaption, FitBCaption::class.java)
        }
        return caption
    }
}
