package com.snaptiongame.snaptionapp;

import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.snaptiongame.snaptionapp.data.converters.SnaptionConverter;
import com.snaptiongame.snaptionapp.data.models.Snaption;

import okhttp3.OkHttpClient;

/**
 * @author Tyler Wong
 */

public class SnaptionApplication extends Application {
   public static Gson gson = setupGson();
   public static OkHttpClient okHttpClient;

   @Override
   public void onCreate() {
      super.onCreate();
   }

   public static OkHttpClient makeOkHttpClient() {
      return okHttpClient = new OkHttpClient();
   }

   public static Gson setupGson() {
      GsonBuilder builder = new GsonBuilder();
      builder.registerTypeAdapter(Snaption.class, new SnaptionConverter());
      builder.excludeFieldsWithoutExposeAnnotation();
      return builder.create();
   }
}
