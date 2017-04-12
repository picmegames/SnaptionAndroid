package com.snaptiongame.app.data.models;

/**
 * @author Tyler Wong
 */

public class OnboardingInfo {
    public int titleId;
    public int descriptionId;
    public int drawableId;

    public static final String TITLE_ID = "title";
    public static final String DESCRIPTION_ID = "description";
    public static final String DRAWABLE_ID = "drawableId";
    public static final String IMAGE_PATH = "imagePath";

    public OnboardingInfo(int titleId, int descriptionId, int drawableId) {
        this.titleId = titleId;
        this.descriptionId = descriptionId;
        this.drawableId = drawableId;
    }
}
