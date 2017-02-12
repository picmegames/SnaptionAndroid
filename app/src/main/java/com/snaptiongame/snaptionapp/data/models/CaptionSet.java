package com.snaptiongame.snaptionapp.data.models;

import android.graphics.drawable.Drawable;

import com.snaptiongame.snaptionapp.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by nickromero on 2/8/17.
 */

public class CaptionSet {

    private ArrayList<Caption> mCaptions;
    private Drawable mSetImage;
    private String mSetName;
    private int mTotalCaptions;
    private int mCaptionsUnlocked;


    public CaptionSet(ArrayList<Caption> captions) {
        mCaptions = captions;
        mCaptionsUnlocked = 0;
        mTotalCaptions = 10;

    }

    public ArrayList<Caption> getCaptions() {
        return mCaptions;
    }

    public void setCaptions(ArrayList<Caption> captions) {
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
