package com.snaptiongame.snaptionapp.data.converters;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.snaptiongame.snaptionapp.data.models.GameInvite;

import org.json.JSONObject;

/**
 * Created by BrianGouldsberry on 2/27/17.
 */

public class BranchConverter {
    public static GameInvite deserializeGameInvite(JSONObject json) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(String.valueOf(json), GameInvite.class);
    }
}
