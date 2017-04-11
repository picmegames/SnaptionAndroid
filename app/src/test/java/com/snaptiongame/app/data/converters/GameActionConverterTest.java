package com.snaptiongame.app.data.converters;

import com.google.gson.JsonObject;
import com.snaptiongame.app.data.models.GameAction;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by nickromero on 2/21/17.
 */

public class GameActionConverterTest {

    private GameAction gameAction;
    private JsonObject jsonObject;
    GameActionConverter converter;

    @Before
    public void setup() {
        gameAction = new GameAction(123, false, GameAction.UPVOTE, GameAction.GAME_ID);

        jsonObject = new JsonObject();

        jsonObject.addProperty(GameAction.GAME_ID, gameAction.targetId);
        jsonObject.addProperty(GameAction.UPVOTE, gameAction.choice);

        converter = new GameActionConverter();
    }

    @Test
    public void testSerialize() {
        assertEquals(converter.serialize(gameAction, GameAction.class, null), jsonObject);
    }
}