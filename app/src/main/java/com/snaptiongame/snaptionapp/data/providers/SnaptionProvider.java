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
   private static SnaptionApiService apiService = SnaptionApiProvider.getApiService();

   public static Observable<List<Snaption>> getAllSnaptions() {
      return apiService.getSnaptions()
               .compose(o -> o.subscribeOn(SnaptionApiProvider.getNetworkScheduler()));
   }

   public static void addSnaption(Snaption snaption) {
      apiService.addSnaption(snaption)
               .compose(o -> o.subscribeOn(SnaptionApiProvider.getNetworkScheduler()));
   }
}
