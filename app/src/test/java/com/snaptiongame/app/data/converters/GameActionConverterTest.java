package com.snaptiongame.app.data.converters;

import com.google.gson.JsonObject;
import com.snaptiongame.app.data.models.GameAction;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * @author Nick Romero
 */

public class GameActionConverterTest {

    private GameAction gameAction;
    private JsonObject jsonObject;
    GameActionConverter converter;

    @Before
    public void setup() {
        gameAction = new GameAction(123, false, GameAction.UPVOTE, GameAction.GAME_ID);

        jsonObject = new JsonObject();

        jsonObject.addProperty(GameAction.GAME_ID, gameAction.getTargetId());
        jsonObject.addProperty(GameAction.GAME_ID, gameAction.getChoice());

        converter = new GameActionConverter();
    }

    @Test
    public void testSerialize() {
        assertEquals(converter.serialize(gameAction, GameAction.class, null), jsonObject);
    }
}
