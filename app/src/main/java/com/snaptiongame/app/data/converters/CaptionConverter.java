package com.snaptiongame.app.data.converters;


import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.snaptiongame.app.data.models.Caption;
import com.snaptiongame.app.data.models.FitBCaption;
import com.snaptiongame.app.data.models.User;

import java.lang.reflect.Type;

/**
 * @author Tyler Wong
 */

public class CaptionConverter implements JsonSerializer<Caption>, JsonDeserializer<Caption> {

    @Override
    public JsonElement serialize(Caption src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject json = new JsonObject();
        json.addProperty(Caption.FITB_ID_SEND, src.fitbIdSend);
        json.addProperty(Caption.CAPTION, src.caption);
        return json;
    }

    @Override
    public Caption deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        JsonObject content = json.getAsJsonObject();
        Caption caption = new Gson().fromJson(json, typeOfT);
        JsonObject creator = content.getAsJsonObject(Caption.CREATOR);

        if (creator.isJsonObject()) {
            caption.creatorName = creator.get(Caption.USERNAME).getAsString();
            JsonObject picture = creator.get(User.PICTURE).getAsJsonObject();
            if (!picture.isJsonNull()) {
                caption.creatorPicture = picture.get(User.IMAGE_URL).getAsString();
            }
        }

        caption.creatorId = creator.get(User.ID).getAsInt();
        JsonElement fitBCaption = content.get(Caption.FITB_OTHER);

        if (fitBCaption.isJsonObject()) {
            caption.assocFitB = new Gson().fromJson(fitBCaption, FitBCaption.class);
        }
        return caption;
    }
}
