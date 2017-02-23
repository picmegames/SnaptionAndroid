package com.snaptiongame.snaptionapp.data.providers;

import com.snaptiongame.snaptionapp.data.models.Caption;
import com.snaptiongame.snaptionapp.data.models.CaptionSet;
import com.snaptiongame.snaptionapp.data.models.FitBCaption;
import com.snaptiongame.snaptionapp.data.models.Like;
import com.snaptiongame.snaptionapp.data.providers.api.ApiProvider;
import com.snaptiongame.snaptionapp.data.api.SnaptionApi;

import java.util.List;

import io.reactivex.Observable;

/**
 * @author Tyler Wong
 */

public class CaptionProvider {
    private static SnaptionApi apiService = ApiProvider.getApiService();

    public static Observable<List<Caption>> getCaptions(int gameId) {
        return apiService.getCaptions(gameId);
    }

    public static Observable<List<FitBCaption>> getFitBCaptions() {
        return apiService.getFitBCaptions();
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
