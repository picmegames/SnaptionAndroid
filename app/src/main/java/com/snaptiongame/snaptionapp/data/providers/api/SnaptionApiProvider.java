package com.snaptiongame.snaptionapp.data.providers.api;

import com.snaptiongame.snaptionapp.data.services.SnaptionApiService;

import retrofit2.Retrofit;
import rx.Scheduler;

/**
 * @author Tyler Wong
 */

public class SnaptionApiProvider {
   private static SnaptionApiService mApiService;
   private static Retrofit.Builder mBuilder;
   private static Scheduler mNetworkScheduler;

   public static SnaptionApiService getApiService() {
      if (mApiService == null) {
         // setBaseUrl(BuildConfig.SNAPTION_SERVER);
      }

      if (mNetworkScheduler == null) {
         // setNetworkScheduler(Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR));
      }

      return mApiService;
   }

   public static Retrofit.Builder setBaseUrl(String baseUrl) {
      mBuilder = mBuilder.baseUrl(baseUrl);
      mApiService = mBuilder.build().create(SnaptionApiService.class);

      return mBuilder;
   }

   public static Retrofit.Builder setNetworkScheduler(Scheduler scheduler) {
      mNetworkScheduler = scheduler;
      // mBuilder = mBuilder.addCallAdapterFactory();
      mApiService = mBuilder.build().create(SnaptionApiService.class);
      return mBuilder;
   }
}
