package com.snaptiongame.snaptionapp.data.providers;

import com.snaptiongame.snaptionapp.data.models.Caption;
import com.snaptiongame.snaptionapp.data.models.Snaption;
import com.snaptiongame.snaptionapp.data.providers.api.SnaptionApiProvider;
import com.snaptiongame.snaptionapp.data.services.SnaptionApiService;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

/**
 * @author Tyler Wong
 */

public class SnaptionProvider {
   private static SnaptionApiService mApiService = SnaptionApiProvider.getApiService();
   public static List<Snaption> testSnaptions = new ArrayList<>();

   public static Observable<List<Snaption>> getAllSnaptions() {
      List<Caption> testCaptions1 = new ArrayList<>();
      Caption testCaption1 = new Caption(0, 0, 1, "test_user_1", 1, "That is a cat in a burrito.");
      Caption testCaption2 = new Caption(1, 0, 2, "test_user_2", 100, "That is a purrito");
      testCaptions1.add(testCaption1);
      testCaptions1.add(testCaption2);
      Snaption testSnaption1 = new Snaption(0, 0, 1, false, "Tyler", 0, null,
            "https://c1.staticflickr.com/3/2389/2073509907_345ad52bc1.jpg", testCaptions1);
      Snaption testSnaption2 = new Snaption(0, 0, 1, false, "Brian", 0, null,
            "http://www.rd.com/wp-content/uploads/sites/2/2016/04/01-cat-wants-to-tell-you-laptop.jpg", testCaptions1);
      Snaption testSnaption3 = new Snaption(0, 0, 1, false, "Nick", 0, null,
            "http://writm.com/wp-content/uploads/2016/08/Cat-hd-wallpapers.jpg", testCaptions1);
      Snaption testSnaption4 = new Snaption(0, 0, 1, false, "Quang", 0, null,
            "http://www.i-love-cats.com/images/2015/04/12/cat-wallpaper-38.jpg", testCaptions1);
      testSnaptions.add(testSnaption1);
      testSnaptions.add(testSnaption2);
      testSnaptions.add(testSnaption3);
      testSnaptions.add(testSnaption4);
      testSnaptions.add(testSnaption1);
      testSnaptions.add(testSnaption2);
      testSnaptions.add(testSnaption3);
      testSnaptions.add(testSnaption4);
      testSnaptions.add(testSnaption1);
      testSnaptions.add(testSnaption2);
      testSnaptions.add(testSnaption3);
      testSnaptions.add(testSnaption4);
      testSnaptions.add(testSnaption1);
      testSnaptions.add(testSnaption2);
      testSnaptions.add(testSnaption3);
      testSnaptions.add(testSnaption4);
      //return mApiService.getSnaptions();
      return Observable.just(testSnaptions);
   }
}
