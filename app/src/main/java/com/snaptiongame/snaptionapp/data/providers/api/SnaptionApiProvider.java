package com.snaptiongame.snaptionapp.data.providers.api;

import android.os.AsyncTask;

import com.snaptiongame.snaptionapp.SnaptionApplication;
import com.snaptiongame.snaptionapp.data.services.SnaptionApiService;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Scheduler;
import rx.schedulers.Schedulers;

/**
 * @author Tyler Wong
 */

public class SnaptionApiProvider {
   private static SnaptionApiService apiService;
   private static Retrofit.Builder builder;
   private static Scheduler networkScheduler;

   private static final String SNAPTION_SERVER_URL = "http://104.198.36.194";

   static {
      builder = new Retrofit.Builder()
            .client(SnaptionApplication.makeOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create(SnaptionApplication.gson));
   }

   public static SnaptionApiService getApiService() {
      if (apiService == null) {
         setBaseUrl(SNAPTION_SERVER_URL);
      }

      if (networkScheduler == null) {
         setNetworkScheduler(Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR));
      }

      return apiService;
   }

   public static Retrofit.Builder setBaseUrl(String baseUrl) {
      builder = builder.baseUrl(baseUrl);
      apiService = builder.build().create(SnaptionApiService.class);

      return builder;
   }

   public static Retrofit.Builder setNetworkScheduler(Scheduler scheduler) {
      networkScheduler = scheduler;
      builder = builder.addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(scheduler));
      apiService = builder.build().create(SnaptionApiService.class);
      return builder;
   }

   public static Scheduler getNetworkScheduler() {
      return networkScheduler;
   }
}
