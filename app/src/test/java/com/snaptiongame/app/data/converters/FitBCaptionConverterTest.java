package com.snaptiongame.app.data.converters;

import com.google.gson.JsonObject;
import com.snaptiongame.app.data.models.FitBCaption;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertTrue;

/**
 * @author Nick Romero
 */

public class FitBCaptionConverterTest {

    private JsonObject jsonObject;
    private FitBCaptionConverter converter;

    @Before
    public void setup() {
        jsonObject = new JsonObject();
        jsonObject.addProperty(FitBCaption.Companion.getID(), 123);
        jsonObject.addProperty(FitBCaption.Companion.getBEFORE(), "Before");
        jsonObject.addProperty(FitBCaption.Companion.getAFTER(), "After");
        jsonObject.addProperty(FitBCaption.Companion.getCHAR_LIMIT(), 100);
        jsonObject.addProperty(FitBCaption.Companion.getSET_ID(), 10);


        converter = new FitBCaptionConverter();

    }

    @Test
    public void testDeserialize() {
        assertTrue(converter.deserialize(jsonObject, FitBCaption.class, null) != null);
    }
}
