package com.snaptiongame.snaptionapp.data.providers;

import com.snaptiongame.snaptionapp.data.models.Snaption;
import com.snaptiongame.snaptionapp.data.providers.api.SnaptionApiProvider;
import com.snaptiongame.snaptionapp.data.services.SnaptionApiService;

import java.util.List;

import rx.Observable;

/**
 * @author Tyler Wong
 */

public class SnaptionProvider {
   private static SnaptionApiService mApiService = SnaptionApiProvider.getApiService();

   public static Observable<List<Snaption>> getAllSnaptions() {
      return mApiService.getSnaptions();
   }
}
