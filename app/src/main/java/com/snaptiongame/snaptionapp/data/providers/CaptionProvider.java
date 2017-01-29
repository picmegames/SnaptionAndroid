package com.snaptiongame.snaptionapp.data.providers;

import com.snaptiongame.snaptionapp.data.models.Caption;
import com.snaptiongame.snaptionapp.data.models.Like;
import com.snaptiongame.snaptionapp.data.providers.api.SnaptionApiProvider;
import com.snaptiongame.snaptionapp.data.services.SnaptionApiService;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import rx.Observable;

/**
 * @author Tyler Wong
 */

public class CaptionProvider {
   private static SnaptionApiService apiService = SnaptionApiProvider.getApiService();

   public static Observable<List<Caption>> getCaptions(int gameId) {
      return apiService.getCaptions(gameId);
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

   public static void upvoteCaption(int captionId, Like request) {
      apiService.upvoteCaption(captionId, request);
   }

   public static Observable<Caption> addCaption(int gameId, Caption caption) {
      return apiService.addCaption(gameId, caption);
   }
}
