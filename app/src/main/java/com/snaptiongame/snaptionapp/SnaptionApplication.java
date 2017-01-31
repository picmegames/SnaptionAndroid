package com.snaptiongame.snaptionapp;

import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.snaptiongame.snaptionapp.data.converters.AddFriendConverter;
import com.snaptiongame.snaptionapp.data.converters.CaptionConverter;
import com.snaptiongame.snaptionapp.data.converters.FriendConverter;
import com.snaptiongame.snaptionapp.data.converters.LikeConverter;
import com.snaptiongame.snaptionapp.data.converters.OAuthConverter;
import com.snaptiongame.snaptionapp.data.converters.SessionConverter;
import com.snaptiongame.snaptionapp.data.converters.SnaptionConverter;
import com.snaptiongame.snaptionapp.data.converters.UserConverter;
import com.snaptiongame.snaptionapp.data.models.AddFriendRequest;
import com.snaptiongame.snaptionapp.data.models.Caption;
import com.snaptiongame.snaptionapp.data.models.Friend;
import com.snaptiongame.snaptionapp.data.models.Like;
import com.snaptiongame.snaptionapp.data.models.OAuthRequest;
import com.snaptiongame.snaptionapp.data.models.Session;
import com.snaptiongame.snaptionapp.data.models.Snaption;
import com.snaptiongame.snaptionapp.data.models.User;
import com.squareup.leakcanary.LeakCanary;

import io.realm.Realm;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import timber.log.Timber;

/**
 * @author Tyler Wong
 */

public class SnaptionApplication extends Application {
   public static Gson gson = setupGson();
   public static OkHttpClient okHttpClient;

   @Override
   public void onCreate() {
      super.onCreate();

      // INIT Leak Canary (Memory leak checking)
      if (LeakCanary.isInAnalyzerProcess(this)) {
         return;
      }

      LeakCanary.install(this);

      // INIT Realm (Local database)
      Realm.init(this);

      if (BuildConfig.DEBUG) {
         // INIT Timber (Logger for debug builds)
         Timber.plant(new Timber.DebugTree());
      }
   }

   public static OkHttpClient makeOkHttpClient() {
      if (BuildConfig.DEBUG) {
         HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
         interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
         okHttpClient = new OkHttpClient.Builder()
               .addInterceptor(interceptor)
               .build();
      }
      else {
         okHttpClient = new OkHttpClient.Builder()
               .build();
      }
      return okHttpClient;
   }

   public static Gson setupGson() {
      GsonBuilder builder = new GsonBuilder();
      builder.registerTypeAdapter(OAuthRequest.class, new OAuthConverter());
      builder.registerTypeAdapter(Session.class, new SessionConverter());
      builder.registerTypeAdapter(User.class, new UserConverter());
      builder.registerTypeAdapter(Snaption.class, new SnaptionConverter());
      builder.registerTypeAdapter(Caption.class, new CaptionConverter());
      builder.registerTypeAdapter(Like.class, new LikeConverter());
      builder.registerTypeAdapter(Friend.class, new FriendConverter());
      builder.registerTypeAdapter(AddFriendRequest.class, new AddFriendConverter());
      builder.excludeFieldsWithoutExposeAnnotation();
      return builder.create();
   }
}
