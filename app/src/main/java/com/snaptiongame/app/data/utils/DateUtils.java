package com.snaptiongame.app.data.utils;

/**
 * @author Tyler Wong
 */

public class DateUtils {

    private static final int MILLIS = 1000;

    public static long getNow() {
        return System.currentTimeMillis() / MILLIS;
    }

    public static boolean isPastDate(long date1, long date2) {
        return date1 <= date2;
    }

    public static boolean isPastNow(long date) {
        return date <= getNow();
    }
}
