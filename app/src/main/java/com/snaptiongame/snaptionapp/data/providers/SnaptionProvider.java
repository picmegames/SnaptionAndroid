package com.snaptiongame.snaptionapp.data.providers;

import com.snaptiongame.snaptionapp.data.models.Snaption;
import com.snaptiongame.snaptionapp.data.providers.api.SnaptionApiProvider;
import com.snaptiongame.snaptionapp.data.services.SnaptionApiService;

import java.util.Collections;
import java.util.List;

import rx.Observable;

/**
 * @author Tyler Wong
 */

public class SnaptionProvider {
   private static SnaptionApiService apiService = SnaptionApiProvider.getApiService();

   private static Observable.Transformer<List<Snaption>, List<Snaption>> sortSnaptions() {
      return (Observable<List<Snaption>> o) -> o.filter(snaptions -> {
         Collections.reverse(snaptions);
         return true;
      });
   }

   protected static Observable.Transformer<List<Snaption>, List<Snaption>> mockSnaptions() {
      return null;
   }

   public static Observable<List<Snaption>> getAllSnaptions() {
      return apiService.getSnaptions()
               .compose(o -> o.subscribeOn(SnaptionApiProvider.getNetworkScheduler()))
               .compose(sortSnaptions());
   }

   public static void addSnaption(String type, String image) {
      apiService.addSnaption(type, image)
            .compose(o -> o.subscribeOn(SnaptionApiProvider.getNetworkScheduler()));
   }
}
