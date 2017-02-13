package com.snaptiongame.snaptionapp.data.providers.api;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.snaptiongame.snaptionapp.SnaptionApplication;
import com.snaptiongame.snaptionapp.data.services.SnaptionApiService;

import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * The Snaption API Provider provides an instance of
 * the API service built by Retrofit.
 *
 * @author Tyler Wong
 * @version 1.0
 */
public class ApiProvider {
   private static SnaptionApiService apiService;

   private static final String SNAPTION_SERVER_URL = "https://104.198.36.194";

   /**
    * This method provides and handles the creation of
    * the Snaption API service.
    *
    * @return An instance of a Snaption API service
    */
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
