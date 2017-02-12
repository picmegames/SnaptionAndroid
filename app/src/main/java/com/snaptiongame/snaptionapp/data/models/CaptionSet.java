package com.snaptiongame.snaptionapp.data.models;

import android.graphics.drawable.Drawable;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by nickromero on 2/8/17.
 */

public class CaptionSet {

    private List<Caption> mCaptions;
    private Drawable mSetImage;
    private String mSetName;
    private int mTotalCaptions;
    private int mCaptionsUnlocked;

    static final int MAX_CAPTIONS = 10;
    static final int MIN_CAPTIONS = 0;


    public CaptionSet(ArrayList<Caption> captions) {
        mCaptions = captions;
        mCaptionsUnlocked = MIN_CAPTIONS;
        mTotalCaptions = MAX_CAPTIONS;

    }

    public List<Caption> getCaptions() {
        return mCaptions;
    }

    public void setCaptions(List<Caption> captions) {
        mCaptions = captions;
    }

    public void setCaptionsUnlocked(int unlocked) {
        mCaptionsUnlocked = unlocked;
    }

    public void setSetName(String mSetName) {
        this.mSetName = mSetName;
    }



    public int getTotalCaptions() {
        return mTotalCaptions;
    }


    public int getCaptionsUnlocked() {
        return mCaptionsUnlocked;
    }

    public String getSetName() {
        return mSetName;
    }
}
