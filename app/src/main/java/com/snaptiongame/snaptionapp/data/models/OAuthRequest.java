package com.snaptiongame.snaptionapp.data.models;

/**
 * @author Tyler Wong
 */

public class OAuthRequest {
   public String token;
   public String deviceToken;
   public String provider;

   public static final String FACEBOOK_TOKEN = "accessToken";
   public static final String GOOGLE_TOKEN = "token";
   public static final String DEVICE_TOKEN = "device_token";
   public static final String PROVIDER = "provider";

   public OAuthRequest(String token, String deviceToken, String provider) {
      this.token = token;
      this.deviceToken = deviceToken;
      this.provider = provider;
   }
}
