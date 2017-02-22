package com.snaptiongame.snaptionapp.data.converters;

import com.google.gson.JsonObject;
import com.snaptiongame.snaptionapp.data.models.Like;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Created by nickromero on 2/21/17.
 */

public class LikeConverterTest {

    private Like like;
    private JsonObject jsonObject;
    LikeConverter converter;

    @Before
    public void setup() {
        like = new Like(123, 124, false, true, "What");

        jsonObject = new JsonObject();

        jsonObject.addProperty(Like.USER_ID, like.userId);
        jsonObject.addProperty(Like.UPVOTE, like.upvote);
        jsonObject.addProperty(Like.FLAGGED, like.flagged);
        jsonObject.addProperty(like.type, like.targetId);

        converter = new LikeConverter();


    }

    @Test
    public void testSerialize() {
        assertEquals(converter.serialize(like, Like.class, null), jsonObject);
    }
}
