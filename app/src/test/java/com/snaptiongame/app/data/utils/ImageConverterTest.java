package com.snaptiongame.app.data.utils;

import org.junit.Test;

import java.io.IOException;

/**
 * @author Tyler Wong
 */

public class ImageConverterTest {
    @Test(expected = RuntimeException.class)
    public void testConvertImage() throws IOException {
        ImageConverter.convertImageBase64(null, null);
    }
}
