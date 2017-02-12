package com.snaptiongame.snaptionapp.data.models;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * @author Tyler Wong
 */

public class CaptionTest {
   @Test
   public void testDefaultConstructor() {
      Caption caption = new Caption();
      assertTrue(caption.id == 0);
   }

   @Test
   public void testConstructorSendCaption() {
      Caption caption = new Caption(0, "", 1);
      assertTrue(caption.fitBId == 0);
      assertTrue(caption.caption.isEmpty());
   }

   @Test
   public void testConstructorCaptio() {
      Caption caption = new Caption(0, 0, 0, 0, 0, "", "");
      assertTrue(caption.id == 0);
   }
}
