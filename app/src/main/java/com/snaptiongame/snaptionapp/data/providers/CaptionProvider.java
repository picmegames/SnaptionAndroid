package com.snaptiongame.snaptionapp.data.providers;

import com.snaptiongame.snaptionapp.data.models.Caption;
import com.snaptiongame.snaptionapp.data.models.CaptionSet;
import com.snaptiongame.snaptionapp.data.models.FitBCaption;
import com.snaptiongame.snaptionapp.data.models.Like;
import com.snaptiongame.snaptionapp.data.providers.api.SnaptionApiProvider;
import com.snaptiongame.snaptionapp.data.services.SnaptionApiService;

import java.util.List;

import io.reactivex.Observable;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * @author Tyler Wong
 */

public class CaptionProvider {
   private static SnaptionApiService apiService = SnaptionApiProvider.getApiService();

   public static Observable<List<Caption>> getCaptions(int gameId) {
      return apiService.getCaptions(gameId);
   }

   public static Observable<List<FitBCaption>> getFitBCaptions() {
      return apiService.getFitBCaptions();
   }

   public static Observable<List<Caption>> getLocalCaptions(int gameId) {
      return Observable.defer(() -> {
         try (Realm realmInstance = Realm.getDefaultInstance()) {
            RealmResults<Caption> realmResults = realmInstance
                  .where(Caption.class)
                  .equalTo("gameId", gameId)
                  .findAll();
            return Observable.just(realmInstance.copyFromRealm(realmResults));
         }
      });
   }

   public static Observable<Like> upvoteCaption(Like request) {
      return apiService.upvoteCaption(request);
   }

   public static Observable<Caption> addCaption(int gameId, Caption caption) {
      return apiService.addCaption(gameId, caption);
   }

   public static Observable<List<CaptionSet>> getCaptionSets() {
      return apiService.getCaptionSets();
   }
}
