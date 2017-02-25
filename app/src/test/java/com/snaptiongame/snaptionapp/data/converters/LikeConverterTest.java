package com.snaptiongame.snaptionapp.data.converters;

import com.google.gson.JsonObject;
import com.snaptiongame.snaptionapp.data.models.Like;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by nickromero on 2/21/17.
 */

public class LikeConverterTest {

    private Like like;
    private JsonObject jsonObject;
    LikeConverter converter;

    @Before
    public void setup() {
        like = new Like(123, false, Like.UPVOTE, Like.GAME_ID);

        jsonObject = new JsonObject();

        jsonObject.addProperty(Like.GAME_ID, like.targetId);
        jsonObject.addProperty(Like.UPVOTE, like.choice);

        converter = new LikeConverter();
    }

    @Test
    public void testSerialize() {
        assertEquals(converter.serialize(like, Like.class, null), jsonObject);
    }
}
