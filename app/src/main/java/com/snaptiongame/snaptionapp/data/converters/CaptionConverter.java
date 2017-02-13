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

import java.lang.reflect.Type;

/**
 * @author Tyler Wong
 */

public class CaptionConverter implements JsonSerializer<Caption>, JsonDeserializer<Caption> {

   @Override
   public JsonElement serialize(Caption src, Type typeOfSrc, JsonSerializationContext context) {
      JsonObject json = new JsonObject();
      json.addProperty(Caption.FITB_ID_SEND, 1);
      json.addProperty(Caption.CAPTION, src.caption);
      json.addProperty(Caption.USER_ID, src.creatorId);
      System.out.println(json.toString());
      return json;
   }

   @Override
   public Caption deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
         throws JsonParseException {
      return new Gson().fromJson(json, typeOfT);
   }
}
