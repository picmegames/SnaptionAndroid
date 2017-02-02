package com.snaptiongame.snaptionapp.data.providers;

import com.snaptiongame.snaptionapp.data.models.Like;
import com.snaptiongame.snaptionapp.data.models.Snaption;
import com.snaptiongame.snaptionapp.data.providers.api.SnaptionApiProvider;
import com.snaptiongame.snaptionapp.data.services.SnaptionApiService;

import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * @author Tyler Wong
 */

public class SnaptionProvider {
   private static SnaptionApiService apiService = SnaptionApiProvider.getApiService();

   public static Observable<List<Snaption>> getAllSnaptions() {
      return apiService.getSnaptions()
            .filter(snaptions -> {
               Collections.reverse(snaptions);
               return true;
            });
   }

   public static Observable<List<Snaption>> getAllLocalSnaptions() {
      return Observable.defer(() -> {
         try (Realm realmInstance = Realm.getDefaultInstance()) {
            RealmResults<Snaption> realmResults = realmInstance
                  .where(Snaption.class)
                  .findAll();
            return Observable.just(realmInstance.copyFromRealm(realmResults));
         }
      });
   }

   public static Observable<Like> upvoteSnaption(Like request) {
      return apiService.upvoteSnaption(request);
   }

   public static Observable<Snaption> addSnaption(Snaption snaption) {
      return apiService.addSnaption(snaption);
   }
}
