package com.snaptiongame.app.data.models;

import com.google.gson.annotations.SerializedName;


/**
 * @author Nick Romero
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
    public int setId;
    @SerializedName(USER_ENTRY)
    public String userEntry;

    public static final String ID = "id";
    public static final String BEFORE = "before";
    public static final String AFTER = "after";
    public static final String CHAR_LIMIT = "charLimit";
    public static final String SET_ID = "set_id";
    public static final String USER_ENTRY = "userEntry";
    public final String placeholderText = "______";

    public FitBCaption(int id, int charLimit, String beforeBlank, String afterBlank, int setId) {
        this.id = id;
        this.beforeBlank = beforeBlank;
        this.afterBlank = afterBlank;
        this.charLimit = charLimit;
        this.setId = setId;
        this.userEntry = "";
    }
    @Override
    public boolean equals(Object obj) {
        return obj instanceof FitBCaption && ((FitBCaption) obj).id == id;
    }
}
