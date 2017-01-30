package com.snaptiongame.snaptionapp.data.providers.api;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.snaptiongame.snaptionapp.SnaptionApplication;
import com.snaptiongame.snaptionapp.data.services.SnaptionApiService;

import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author Tyler Wong
 */

public class SnaptionApiProvider {
   private static SnaptionApiService apiService;

   private static final String SNAPTION_SERVER_URL = "http://104.198.36.194";

   public static SnaptionApiService getApiService() {
      if (apiService == null) {
         apiService = new Retrofit.Builder()
               .baseUrl(SNAPTION_SERVER_URL)
               .client(SnaptionApplication.makeOkHttpClient())
               .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
               .addConverterFactory(GsonConverterFactory.create(SnaptionApplication.gson))
               .build()
               .create(SnaptionApiService.class);
      }

      return apiService;
   }
}
