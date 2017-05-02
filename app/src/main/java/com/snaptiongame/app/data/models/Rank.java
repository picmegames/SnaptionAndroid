package com.snaptiongame.app.data.models;

import com.google.gson.annotations.SerializedName;

/**
 * @author Tyler Wong
 */

public class Rank {
    @SerializedName(ID)
    public int id;
    @SerializedName(TITLE)
    public String title;

    public static final String ID = "id";
    public static final String TITLE = "title";
}
