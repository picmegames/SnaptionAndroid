package com.snaptiongame.snaptionapp.data.models;

/**
 * @author Tyler Wong
 */

public class OAuthRequest {
   public String token;
   public String deviceToken;
   public String provider;

   public OAuthRequest(String token, String deviceToken, String provider) {
      this.token = token;
      this.deviceToken = deviceToken;
      this.provider = provider;
   }
}
