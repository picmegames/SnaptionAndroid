package com.snaptiongame.snaptionapp.data.converters;


import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.snaptiongame.snaptionapp.data.models.Caption;
import com.snaptiongame.snaptionapp.data.models.FitBCaption;

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
      json.addProperty(Caption.USER_ID, src.creatorId);
      return json;
   }

   @Override
   public Caption deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
         throws JsonParseException {
      JsonObject content = json.getAsJsonObject();
      Caption caption = new Gson().fromJson(json, typeOfT);
      JsonElement creator = content.get("creator");
      if (creator.isJsonObject()) {
         caption.creatorName = creator.getAsJsonObject().get("username").getAsString();
         JsonElement picture = creator.getAsJsonObject().get("picture");
         if (!picture.isJsonNull()) {
            caption.creatorPicture = picture.getAsString();
         }
      }
      JsonElement fitBCaption = content.get("fitb");
      if (fitBCaption.isJsonObject()) {
         caption.assocFitB = new Gson().fromJson(fitBCaption, FitBCaption.class);
      }
      return caption;
   }
}
