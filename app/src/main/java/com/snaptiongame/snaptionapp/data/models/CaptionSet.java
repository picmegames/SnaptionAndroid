package com.snaptiongame.snaptionapp.data.models;


import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by nickromero on 2/8/17.
 */

public class CaptionSet {

    @SerializedName(ID)
    public int id;
    @SerializedName(TITLE)
    public String captionSetTitle;
    @SerializedName(ACTIVE)
    public boolean isCaptionSetActive;

    public static final String TITLE = "title";
    public static final String ID = "id";
    public static final String ACTIVE = "active";

    private int mTotalCaptions;
    private int mCaptionsUnlocked;

    private static final int MAX_CAPTIONS = 10;
    private static final int MIN_CAPTIONS = 0;

    public CaptionSet(List<Caption> captions) {

        mCaptionsUnlocked = MIN_CAPTIONS;
        mTotalCaptions = MAX_CAPTIONS;

    }

    public List<Caption> getCaptions() {

        return new ArrayList<>();
    }


    public int getTotalCaptions() {
        return MAX_CAPTIONS;
    }

    public int getCaptionsUnlocked() {
        return mCaptionsUnlocked;
    }

    public String getSetName() { return captionSetTitle; }

}
