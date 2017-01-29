package com.snaptiongame.snaptionapp.data.services;

import com.snaptiongame.snaptionapp.data.models.Caption;
import com.snaptiongame.snaptionapp.data.models.Like;
import com.snaptiongame.snaptionapp.data.models.OAuthRequest;
import com.snaptiongame.snaptionapp.data.models.Session;
import com.snaptiongame.snaptionapp.data.models.Snaption;

import java.util.List;

import retrofit2.http.Body;
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
   Observable<Like> upvoteSnaption(@Path("gameId") int gameId,
                                   @Body Like request);

   @POST("/Games/")
   Observable<Snaption> addSnaption(@Body Snaption snaption);

   @GET("/Games/{gameId}/Captions/")
   Observable<List<Caption>> getCaptions(@Path("gameId") int gameId);

   @POST("/Games/{game_id}/Captions/")
   Observable<Caption> addCaption(@Path("game_id") int gameId, @Body Caption caption);

   @PUT("/Captions/{captionId}/")
   Observable<Like> upvoteCaption(@Path("captionId") int captionId,
                                  @Body Like request);
}
