package com.snaptiongame.app.data.converters

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import com.snaptiongame.app.data.models.ActivityFeedItem
import com.snaptiongame.app.data.models.Caption
import com.snaptiongame.app.data.models.Game
import com.snaptiongame.app.data.models.User
import com.snaptiongame.app.data.providers.api.ApiProvider

import java.lang.reflect.Type

/**
 * @author Tyler Wong
 */

class ActivityFeedItemConverter : JsonDeserializer<ActivityFeedItem> {
    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): ActivityFeedItem {
        val activityObject = json.asJsonObject
        val gson = ApiProvider.gson

        val friend = gson!!.fromJson(activityObject.get(ActivityFeedItem.FRIEND), User::class.java)
        val game = gson.fromJson(activityObject.get(ActivityFeedItem.GAME), Game::class.java)
        val caption = gson.fromJson(activityObject.get(ActivityFeedItem.CAPTION), Caption::class.java)

        return ActivityFeedItem(activityObject.get(ActivityFeedItem.ID).asInt,
                activityObject.get(ActivityFeedItem.DATE).asLong,
                activityObject.get(ActivityFeedItem.TYPE).asInt, friend, game, caption)
    }
}
