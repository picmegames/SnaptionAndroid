package com.snaptiongame.snaptionapp.data.models;

/**
 * @author Tyler Wong
 */

public class OAuthRequest {
    public String token;
    public String deviceToken;
    public String deviceType;
    public String linkToken;

    public static final String TOKEN = "token";
    public static final String DEVICE_TOKEN = "device_token";
    public static final String DEVICE_TYPE = "device_type";
    public static final String ANDROID = "Android";
    public static final String LINK_TOKEN = "linkToken";

    public OAuthRequest(String token, String deviceToken, String linkToken) {
        this.token = token;
        this.deviceToken = deviceToken;
        this.deviceType = ANDROID;
        this.linkToken = linkToken;
    }
}
