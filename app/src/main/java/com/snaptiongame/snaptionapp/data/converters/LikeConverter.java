package com.snaptiongame.snaptionapp.data.converters;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.snaptiongame.snaptionapp.data.models.LikeRequest;

import java.lang.reflect.Type;

/**
 * @author Tyler Wong
 */

public class LikeConverter implements JsonSerializer<LikeRequest> {
   private static final String USER_ID = "userId";
   private static final String UPVOTE = "upvote";

   @Override
   public JsonElement serialize(LikeRequest src, Type typeOfSrc, JsonSerializationContext context) {
      JsonObject json = new JsonObject();
      json.addProperty(USER_ID, src.userId);
      json.addProperty(UPVOTE, src.upvote);
      return json;
   }
}
