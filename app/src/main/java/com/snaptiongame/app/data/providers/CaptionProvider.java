package com.snaptiongame.app.data.providers;

import com.snaptiongame.app.data.models.Caption;
import com.snaptiongame.app.data.models.CaptionSet;
import com.snaptiongame.app.data.models.FitBCaption;
import com.snaptiongame.app.data.models.Like;
import com.snaptiongame.app.data.providers.api.ApiProvider;
import com.snaptiongame.app.data.api.SnaptionApi;

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

    public static Observable<List<FitBCaption>> getFitBCaptions(int setId) {
        return apiService.getFitBCaptions(setId);
    }

    public static Observable<Like> upvoteOrFlagCaption(Like request) {
        return apiService.upvoteOrFlagCaption(request);
    }

    public static Observable<Caption> addCaption(int gameId, Caption caption) {
        return apiService.addCaption(gameId, caption);
    }

    public static Observable<List<CaptionSet>> getCaptionSets() {
        return apiService.getCaptionSets();
    }
}