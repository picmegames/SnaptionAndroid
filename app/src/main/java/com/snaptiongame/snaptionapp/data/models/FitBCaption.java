package com.snaptiongame.snaptionapp.data.models;

import com.google.gson.annotations.SerializedName;


/**
 * Created by nickromero on 2/10/17.
 */

public class FitBCaption {
    @SerializedName(ID)
    public int id;
    @SerializedName(BEFORE)
    public String beforeBlank;
    @SerializedName(AFTER)
    public String afterBlank;
    @SerializedName(CHAR_LIMIT)
    public int charLimit;
    @SerializedName(SET_ID)
    public int setID;
    @SerializedName(USER_ENTRY)
    public String userEntry;

    public static final String ID = "id";
    public static final String BEFORE = "before";
    public static final String AFTER = "after";
    public static final String CHAR_LIMIT = "charLimit";
    public static final String SET_ID = "set_id";
    public static final String USER_ENTRY = "userEntry";
    public final String placeholderText = "______";

    public FitBCaption(int id, int charLimit, String beforeBlank, String afterBlank, int setID) {
        this.id = id;
        this.beforeBlank = beforeBlank;
        this.afterBlank = afterBlank;
        this.charLimit = charLimit;
        this.setID = setID;
        this.userEntry = "";
    }
}
