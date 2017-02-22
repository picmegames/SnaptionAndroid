package com.snaptiongame.snaptionapp.data.converters;

import com.google.gson.JsonObject;
import com.snaptiongame.snaptionapp.data.models.CaptionSet;
import com.snaptiongame.snaptionapp.data.models.FitBCaption;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Created by nickromero on 2/21/17.
 */

public class FitBCaptionConverterTest {

    private JsonObject jsonObject;
    private FitBCaptionConverter converter;

    @Before
    public void setup() {
        jsonObject = new JsonObject();
        jsonObject.addProperty(FitBCaption.ID, 123);
        jsonObject.addProperty(FitBCaption.BEFORE, "Before");
        jsonObject.addProperty(FitBCaption.AFTER, "After");
        jsonObject.addProperty(FitBCaption.CHAR_LIMIT, 100);
        jsonObject.addProperty(FitBCaption.SET_ID, 10);


        converter = new FitBCaptionConverter();

    }

    @Test
    public void testDeserialize() {
        assertTrue(converter.deserialize(jsonObject, FitBCaption.class, null) != null);
    }
}
