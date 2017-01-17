package com.snaptiongame.snaptionapp.data.converters;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.snaptiongame.snaptionapp.data.models.Caption;
import com.snaptiongame.snaptionapp.data.models.Snaption;

import java.lang.reflect.Type;

/**
 * @author Tyler Wong
 */

public class SnaptionConverter implements JsonDeserializer<Snaption> {
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
