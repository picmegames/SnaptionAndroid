package com.snaptiongame.snaptionapp.data.providers;

import com.snaptiongame.snaptionapp.data.models.Caption;
import com.snaptiongame.snaptionapp.data.providers.api.SnaptionApiProvider;
import com.snaptiongame.snaptionapp.data.services.SnaptionApiService;

import java.util.List;

import rx.Observable;

/**
 * @author Tyler Wong
 */

public class CaptionProvider {
   private static SnaptionApiService apiService = SnaptionApiProvider.getApiService();

   public static Observable<List<Caption>> getCaptions(int gameId) {
      return apiService.getCaptions(gameId)
            .compose(o -> o.subscribeOn(SnaptionApiProvider.getNetworkScheduler()));
   }
}
