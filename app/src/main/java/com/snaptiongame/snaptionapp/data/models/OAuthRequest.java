package com.snaptiongame.snaptionapp.data.models;

/**
 * @author Tyler Wong
 */

public class OAuthRequest {
   public String token;
   public String deviceToken;
   public String deviceType;

   public static final String TOKEN = "token";
   public static final String DEVICE_TOKEN = "device_token";
   public static final String DEVICE_TYPE = "device_type";
   public static final String ANDROID = "Android";

   public OAuthRequest(String token, String deviceToken) {
      this.token = token;
      this.deviceToken = deviceToken;
      this.deviceType = ANDROID;
   }
}
