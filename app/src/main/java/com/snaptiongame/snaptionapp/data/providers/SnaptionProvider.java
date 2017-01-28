package com.snaptiongame.snaptionapp.data.providers;

import com.snaptiongame.snaptionapp.data.models.Snaption;
import com.snaptiongame.snaptionapp.data.models.LikeRequest;
import com.snaptiongame.snaptionapp.data.providers.api.SnaptionApiProvider;
import com.snaptiongame.snaptionapp.data.services.SnaptionApiService;

import java.util.Collections;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
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
            .compose(sortSnaptions());
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

   public static void upvoteSnaption(int gameId, LikeRequest request) {
      apiService.upvoteSnaption(gameId, request);
   }

   public static void addSnaption(String type, String image) {
      apiService.addSnaption(type, image);
   }
}
