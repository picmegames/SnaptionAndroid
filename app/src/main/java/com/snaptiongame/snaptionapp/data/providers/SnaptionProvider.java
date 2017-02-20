package com.snaptiongame.snaptionapp.data.providers;

import com.snaptiongame.snaptionapp.data.models.Like;
import com.snaptiongame.snaptionapp.data.models.Snaption;
import com.snaptiongame.snaptionapp.data.providers.api.ApiProvider;
import com.snaptiongame.snaptionapp.data.services.SnaptionApiService;

import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;

/**
 * @author Tyler Wong
 */

public class SnaptionProvider {
    private static SnaptionApiService apiService = ApiProvider.getApiService();

    public static Observable<List<Snaption>> getAllSnaptions() {
        return apiService.getSnaptions()
                .filter(snaptions -> {
                    Collections.reverse(snaptions);
                    return true;
                });
    }

    public static Observable<Like> upvoteSnaption(Like request) {
        return apiService.upvoteSnaption(request);
    }

    public static Observable<Snaption> addSnaption(Snaption snaption) {
        return apiService.addSnaption(snaption);
    }
}
