package com.snaptiongame.snaptionapp.data.services;

import com.snaptiongame.snaptionapp.data.models.Caption;
import com.snaptiongame.snaptionapp.data.models.Snaption;

import java.util.List;

import retrofit2.http.Body;
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

   @GET("/api/snaptions/{id}")
   Observable<Snaption> getSnaption(@Path("id") String id);

   @GET("/api/snaptions/{id}/captions")
   Observable<List<Caption>> getCaptions(@Path("id") String id);

   @POST("/api/snaptions/{id}/caption")
   Observable<Void> addCaption(String id, @Body Caption caption);
}
