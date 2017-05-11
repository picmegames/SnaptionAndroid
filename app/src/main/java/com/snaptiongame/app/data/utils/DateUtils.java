package com.snaptiongame.app.data.utils;

/**
 * @author Tyler Wong
 */

public class DateUtils {

    public static final long MILLIS = 1000;
    public static final long TWO_WEEKS_OFFSET = 1123200000;
    public static final long TWO_WEEKS = 1209600000;
    public static final long ONE_DAY = 86400000;

    public static long getNow() {
        return System.currentTimeMillis() / MILLIS;
    }

    public static boolean isPastDate(long date1, long date2) {
        return date1 <= date2;
    }

    public static boolean isPastNow(long date) {
        return date <= getNow();
    }

    public static int getDaysRemaining(long date) {
        long dateDiff = date - getNow();
        if (dateDiff <= 0) {
            return 0;
        }
        return (int) Math.ceil(dateDiff / (ONE_DAY / MILLIS));
    }
}
