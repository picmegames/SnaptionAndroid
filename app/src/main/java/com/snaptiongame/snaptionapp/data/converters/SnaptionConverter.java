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
import com.snaptiongame.snaptionapp.data.models.Snaption;

import java.lang.reflect.Type;

/**
 * @author Tyler Wong
 */

public class SnaptionConverter implements JsonSerializer<Snaption>, JsonDeserializer<Snaption> {

   @Override
   public JsonElement serialize(Snaption src, Type typeOfSrc, JsonSerializationContext context) {
      JsonObject json = new JsonObject();
      json.addProperty(Snaption.IS_PUBLIC, src.isPublic);
      json.addProperty(Snaption.RATING, src.rating);
      json.addProperty(Snaption.PICTURE, src.picture);
      json.addProperty(Snaption.IMG_TYPE, src.type);
      return json;
   }

   @Override
   public Snaption deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
         throws JsonParseException {
      JsonObject content = json.getAsJsonObject();
      Snaption snaption = new Gson().fromJson(json, typeOfT);
      JsonElement topCaption = content.get("topCaption");
      snaption.topCaption = new Gson().fromJson(topCaption, Caption.class);
      return snaption;
   }
}
