package com.snaptiongame.snaptionapp.data.converters;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.snaptiongame.snaptionapp.data.models.Caption;

import java.lang.reflect.Type;

/**
 * @author Tyler Wong
 */

public class CaptionConverter implements JsonDeserializer<Caption> {
   @Override
   public Caption deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
         throws JsonParseException {
      JsonObject content = json.getAsJsonObject();
      Caption caption = new Gson().fromJson(json, typeOfT);
//      JsonElement fields = content.get("fields");
//      caption.meta = new Gson().fromJson(fields, CaptionMeta.class);
      return caption;
   }
}
