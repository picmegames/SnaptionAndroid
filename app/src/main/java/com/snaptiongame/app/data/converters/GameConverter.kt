package com.snaptiongame.app.data.converters

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.snaptiongame.app.data.models.Caption
import com.snaptiongame.app.data.models.FitBCaption
import com.snaptiongame.app.data.models.Game
import com.snaptiongame.app.data.models.Tag
import com.snaptiongame.app.data.models.User

import java.lang.reflect.Type
import java.util.ArrayList

/**
 * @author Tyler Wong
 */

class GameConverter : JsonSerializer<Game>, JsonDeserializer<Game> {

    override fun serialize(src: Game, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        val json = JsonObject()
        json.addProperty(Game.IS_PUBLIC, src.isPublic)
        json.addProperty(Game.GAME_DURATION, src.gameDuration)

        if (!src.isFromAnotherGame) {
            json.addProperty(Game.PICTURE, src.picture)
            json.addProperty(Game.IMG_TYPE, src.type)
        }
        else {
            json.addProperty(Game.GAME_ID, src.gameId)
        }

        val tags = JsonArray()
        src.sendTags.forEach { tags.add(it) }
        json.add(Game.TAGS, tags)

        val friends = JsonArray()
        src.friendIds.forEach { friends.add(it) }
        json.add(Game.FRIENDS, friends)
        return json
    }

    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Game {
        val content = json.asJsonObject
        val creatorObject = content.get(Game.PICKER).asJsonObject

        val game = Game(
                content.get(Game.ID).asInt,
                content.get(Game.START_DATE).asLong,
                content.get(Game.IS_PUBLIC).asBoolean,
                creatorObject.get(Game.ID).asInt, "", "")

        game.numUpvotes = content.get(Game.NUM_UPVOTES).asInt
        game.creatorName = creatorObject.get(User.USERNAME).asString

        if (!creatorObject.get(User.PICTURE).isJsonNull) {
            game.creatorImage = creatorObject.get(User.PICTURE).asJsonObject.get(User.IMAGE_URL).asString
        }

        val picture = content.getAsJsonObject(Game.PICTURE)
        game.imageUrl = picture.get(Game.IMAGE_URL).asString
        game.imageWidth = picture.get(Game.IMAGE_WIDTH).asInt
        game.imageHeight = picture.get(Game.IMAGE_HEIGHT).asInt

        val endDate = content.get(Game.END_DATE)
        if (endDate.isJsonNull) {
            game.endDate = 0
        }
        else {
            game.endDate = endDate.asLong
        }

        val tags = content.getAsJsonArray(Game.TAGS)
        tags.forEach { game.tags.add(Gson().fromJson(it, Tag::class.java)) }

        val users = content.getAsJsonArray(Game.USERS)
        var userObject: JsonObject
        var pictureObject: JsonElement
        val gameUsers = ArrayList<User>()
        var newUser: User

        if (users.size() > 0) {
            users.forEach {
                newUser = User()
                userObject = it.asJsonObject
                newUser.id = userObject.get(User.ID).asInt
                newUser.username = userObject.get(User.USERNAME).asString
                newUser.fullName = userObject.get(User.FULL_NAME).asString
                pictureObject = userObject.get(User.PICTURE)
                if (!pictureObject.isJsonNull) {
                    newUser.imageUrl = pictureObject.asJsonObject.get(User.IMAGE_URL).asString
                    newUser.imageWidth = pictureObject.asJsonObject.get(User.IMAGE_WIDTH).asInt
                    newUser.imageHeight = pictureObject.asJsonObject.get(User.IMAGE_HEIGHT).asInt
                }
                gameUsers.add(newUser)
            }
        }
        game.users = gameUsers

        if (content.get(Game.BEEN_UPVOTED) != null && content.get(Game.BEEN_FLAGGED) != null) {
            game.beenUpvoted = content.get(Game.BEEN_UPVOTED).asBoolean
            game.beenFlagged = content.get(Game.BEEN_FLAGGED).asBoolean
        }

        val topCaption = content.get(Game.TOP_CAPTION)

        if (topCaption != null && topCaption.isJsonObject) {
            val caption = Caption()
            caption.assocFitB = FitBCaption(0, 0,
                    topCaption.asJsonObject.get(Game.FITB_BEFORE).asString,
                    topCaption.asJsonObject.get(Game.FITB_AFTER).asString, 0)
            caption.caption = topCaption.asJsonObject.get(Caption.CAPTION).asString
            val topCaptionerPicture = topCaption.asJsonObject.get(Caption.USER_PICTURE)

            if (!topCaptionerPicture.isJsonNull) {
                caption.creatorPicture = topCaptionerPicture.asJsonObject.get(User.IMAGE_URL).asString
            }

            caption.creatorId = topCaption.asJsonObject.get(Caption.USER_ID).asInt
            caption.creatorName = topCaption.asJsonObject.get(Caption.USERNAME).asString
            game.topCaption = caption
        }
        return game
    }
}
