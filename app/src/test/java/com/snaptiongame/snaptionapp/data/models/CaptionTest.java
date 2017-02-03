package com.snaptiongame.snaptionapp.data.models;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * @author Tyler Wong
 */

public class CaptionTest {
   @Test
   public void testConstructorSendCaption() {
      Caption caption = new Caption(0, "");
      assertTrue(caption.fitBId == 0);
      assertTrue(caption.caption.isEmpty());
   }
}
