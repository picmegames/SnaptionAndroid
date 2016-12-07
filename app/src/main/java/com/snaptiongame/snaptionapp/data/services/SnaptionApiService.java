package com.snaptiongame.snaptionapp.data.services;

import com.snaptiongame.snaptionapp.data.models.Caption;
import com.snaptiongame.snaptionapp.data.models.Snaption;

import java.util.List;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

/**
 * @author Tyler Wong
 */

public interface SnaptionApiService {
   @GET("/games")
   Observable<List<Snaption>> getSnaptions();

   @FormUrlEncoded
   @POST("/games")
   Observable<Void> addSnaption(@Field("type") String type, @Field("pictureEncoded") String image);

   @GET("/captions/{gameId}")
   Observable<List<Caption>> getCaptions(@Path("gameId") int gameId);

   @FormUrlEncoded
   @POST("/captions")
   Observable<Void> addCaption(@Field("message") String message, @Field("gameId") int gameId);
}
