package com.snaptiongame.app.data.models;


import com.google.gson.annotations.SerializedName;


/**
 * @author Nick Romero
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

    public static final int MAX_CAPTIONS = 10;
    public static final int MIN_CAPTIONS = 0;

    public CaptionSet() {

    }

    public String getSetName() {
        return captionSetTitle;
    }
}
