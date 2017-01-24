package com.snaptiongame.snaptionapp.data.providers;

import android.os.Bundle;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.snaptiongame.snaptionapp.data.models.Friend;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import rx.Observable;

/**
 * @author Tyler Wong
 */

public class FriendProvider {

   public static void loadUserFriends() {
      AccessToken token = AccessToken.getCurrentAccessToken();
      GraphRequest graphRequest = GraphRequest.newMeRequest(token, (JSONObject object, GraphResponse response) -> {
         try {
            JSONArray jsonArrayFriends = object.getJSONObject("friends").getJSONArray("data");
            for (int index = 0; index < jsonArrayFriends.length(); index++) {
               storeFriendInfo(jsonArrayFriends.getJSONObject(index).getString("id"));
            }
         }
         catch (JSONException e) {
            e.printStackTrace();
         }
      });
      Bundle params = new Bundle();
      params.putString("fields", "friends");
      graphRequest.setParameters(params);
      graphRequest.executeAsync();
   }

   private static void storeFriendInfo(String id) {
      AccessToken token = AccessToken.getCurrentAccessToken();
      GraphRequest graphRequest = new GraphRequest(token, id, null, HttpMethod.GET, graphResponse -> {
         JSONObject object = graphResponse.getJSONObject();
         try {
            String fullName = object.getString("name");
            String firstName = object.getString("first_name");
            String lastName = object.getString("last_name");
            String picture = object.getJSONObject("picture").getJSONObject("data").getString("url");
            String cover = object.getJSONObject("cover").getString("source");

            Friend newFriend = new Friend(id, firstName, lastName, fullName, "", picture, cover, "");
            try (Realm realmInstance = Realm.getDefaultInstance()) {
               realmInstance.executeTransaction(realm -> realm.copyToRealmOrUpdate(newFriend));
            }
         }
         catch (JSONException e) {
            e.printStackTrace();
         }
      });
      Bundle param = new Bundle();
      param.putString("fields", "name, first_name, last_name, picture, cover, email");
      graphRequest.setParameters(param);
      graphRequest.executeAsync();
   }

   public static Observable<List<Friend>> getFacebookFriends() {
      return Observable.defer(() -> {
         try (Realm realmInstance = Realm.getDefaultInstance()) {
            RealmResults<Friend> realmResults = realmInstance
                  .where(Friend.class)
                  .findAll();
            return Observable.just(realmInstance.copyFromRealm(realmResults));
         }
      });
   }
}
