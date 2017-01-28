package com.snaptiongame.snaptionapp.data.converters;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.snaptiongame.snaptionapp.data.authentication.AuthenticationManager;
import com.snaptiongame.snaptionapp.data.models.OAuthRequest;

import java.lang.reflect.Type;

/**
 * @author Tyler Wong
 */

public class OAuthConverter implements JsonSerializer<OAuthRequest> {
   private static final String FACEBOOK_TOKEN = "accessToken";
   private static final String GOOGLE_TOKEN = "token";
   private static final String DEVICE_TOKEN = "device_token";
   private static final String PROVIDER = "provider";

   @Override
   public JsonElement serialize(OAuthRequest src, Type typeOfSrc, JsonSerializationContext context) {
      JsonObject json = new JsonObject();
      if (src.provider.equals(AuthenticationManager.FACEBOOK_LOGIN)) {
         json.addProperty(FACEBOOK_TOKEN, src.token);
      }
      else {
         json.addProperty(GOOGLE_TOKEN, src.token);
      }
      json.addProperty(DEVICE_TOKEN, src.deviceToken);
      json.addProperty(PROVIDER, src.provider);
      return json;
   }
}
