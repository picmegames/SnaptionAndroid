package com.snaptiongame.app.data.converters;

import com.google.gson.JsonObject;
import com.snaptiongame.app.data.models.CaptionSet;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * @author Nick Romero
 */

public class CaptionSetConverterTest {

    private JsonObject jsonObject;
    private CaptionSet captionSet;
    private CaptionSetConverter converter;

    @Before
    public void setup() {
        jsonObject = new JsonObject();
        jsonObject.addProperty(CaptionSet.TITLE, "A Caption Set");
        jsonObject.addProperty(CaptionSet.ID, 123);
        jsonObject.addProperty(CaptionSet.ACTIVE, false);

        captionSet = new CaptionSet();
        captionSet.setCaptionSetActive(false);
        captionSet.setId(123);
        captionSet.setSetName("A Caption Set");

        converter = new CaptionSetConverter();


    }

    @Test
    public void testSerialize() {
        assertEquals(jsonObject,  converter.serialize(captionSet, null, null));
    }

    @Test
    public void testDeserialize() {
        assertTrue(converter.deserialize(jsonObject, CaptionSet.class, null) != null);
    }
}
