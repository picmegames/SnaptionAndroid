package com.snaptiongame.app.data.models;

import com.google.gson.annotations.SerializedName;

/**
 * @author Tyler Wong
 */

public class Tag {

    @SerializedName(ID)
    public int id;
    @SerializedName(NAME)
    public String name;

    public static final String ID = "id";
    public static final String NAME = "name";
}
