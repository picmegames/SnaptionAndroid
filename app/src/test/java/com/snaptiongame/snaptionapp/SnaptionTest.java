package com.snaptiongame.snaptionapp;

import com.snaptiongame.snaptionapp.data.models.Snaption;
import com.snaptiongame.snaptionapp.data.models.SnaptionMeta;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by BrianGouldsberry on 1/12/17.
 */

public class SnaptionTest {
    @Test
    public void displayCaption_isCorrect() throws Exception {
        Snaption mock = new Snaption(0, new SnaptionMeta(0, 0, false, "", 0, "", "", "", "test"));
        assertEquals("test", mock.getDisplayCaption());
        mock = new Snaption(0, new SnaptionMeta(0, 0, false, "", 0, "", "", "",
                "123456789012345678901234567890123456789012345678901234567890"));
        assertEquals("123456789012345678901234567890123456789012345678901234567890", mock.getDisplayCaption());
        mock = new Snaption(0, new SnaptionMeta(0, 0, false, "", 0, "", "", "",
                "1234567890123456789012345678901234567890123456789012345678901"));
        assertEquals("123456789012345678901234567890123456789012345678901234567...", mock
                .getDisplayCaption());
        mock = new Snaption(0, new SnaptionMeta(0, 0, false, "", 0, "", "", "",
                "A very long message that is definitely longer than the 60 character limit " +
                        "by a slightly large margin"));
        assertEquals("A very long message that is definitely longer than the 60...", mock
                .getDisplayCaption());
    }
}
