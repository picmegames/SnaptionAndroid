package com.snaptiongame.app.data.models;

/**
 * @author Tyler Wong
 */

public class OnboardingInfo {
    public int titleId;
    public int descriptionId;
    public int animationId;

    public static final String TITLE_ID = "title";
    public static final String DESCRIPTION_ID = "description";
    public static final String ANIMATION = "animation";

    public OnboardingInfo(int titleId, int descriptionId, int animationId) {
        this.titleId = titleId;
        this.descriptionId = descriptionId;
        this.animationId = animationId;
    }
}
