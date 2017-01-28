package com.snaptiongame.snaptionapp.data.services;

import com.snaptiongame.snaptionapp.data.models.Caption;
import com.snaptiongame.snaptionapp.data.models.LikeRequest;
import com.snaptiongame.snaptionapp.data.models.OAuthRequest;
import com.snaptiongame.snaptionapp.data.models.Session;
import com.snaptiongame.snaptionapp.data.models.Snaption;

import java.util.List;

import retrofit2.http.Body;
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

   @POST("/OAuth/")
   Observable<Session> userOAuthFacebook(@Body OAuthRequest request);

   @POST("/OAuth/Google/")
   Observable<Session> userOAuthGoogle(@Body OAuthRequest request);

   @GET("/Games/")
   Observable<List<Snaption>> getSnaptions();

   @PUT("/Games/{gameId}/")
   Observable<Void> upvoteSnaption(@Path("gameId") int gameId,
                                   @Body LikeRequest request);

   @FormUrlEncoded
   @POST("/Games/")
   Observable<Void> addSnaption(@Field("type") String type,
                                @Field("pictureEncoded") String image);

   @GET("/Games/{gameId}/Captions/")
   Observable<List<Caption>> getCaptions(@Path("gameId") int gameId);

   @PUT("/Captions/{captionId}/")
   Observable<Void> upvoteCaption(@Path("captionId") int captionId,
                                  @Body LikeRequest request);

   @FormUrlEncoded
   @POST("/Captions/")
   Observable<Void> addCaption(@Field("message") String message,
                               @Field("gameId") int gameId);
}
