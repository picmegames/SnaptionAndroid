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

   @Override
   public JsonElement serialize(OAuthRequest src, Type typeOfSrc, JsonSerializationContext context) {
      JsonObject json = new JsonObject();
      if (src.provider.equals(AuthenticationManager.FACEBOOK_LOGIN)) {
         json.addProperty(OAuthRequest.FACEBOOK_TOKEN, src.token);
      }
      else {
         json.addProperty(OAuthRequest.GOOGLE_TOKEN, src.token);
      }
      json.addProperty(OAuthRequest.DEVICE_TOKEN, src.deviceToken);
      json.addProperty(OAuthRequest.PROVIDER, src.provider);
      return json;
   }
}
