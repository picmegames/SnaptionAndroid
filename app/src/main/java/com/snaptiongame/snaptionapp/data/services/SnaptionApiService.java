package com.snaptiongame.snaptionapp.data.services;

import com.snaptiongame.snaptionapp.data.models.Caption;
import com.snaptiongame.snaptionapp.data.models.Snaption;

import java.util.List;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import rx.Observable;

/**
 * @author Tyler Wong
 */

public interface SnaptionApiService {
   @GET("/Games/")
   Observable<List<Snaption>> getSnaptions();

   @FormUrlEncoded
   @PUT("/Games/{gameId}/")
   Observable<Void> upvoteSnaption(@Path("gameId") int gameId, @Field("upvote") boolean upvote, @Field("userId") int userId);

   @FormUrlEncoded
   @POST("/Games/")
   Observable<Void> addSnaption(@Field("type") String type, @Field("pictureEncoded") String image);

   @GET("/Games/{gameId}/Captions/")
   Observable<List<Caption>> getCaptions(@Path("gameId") int gameId);

   @FormUrlEncoded
   @PUT("/Captions/{captionId}/")
   Observable<Void> upvoteCaption(@Path("captionId") int captionId, @Field("upvote") boolean upvote);

   @FormUrlEncoded
   @POST("/Captions")
   Observable<Void> addCaption(@Field("message") String message, @Field("gameId") int gameId);
}
