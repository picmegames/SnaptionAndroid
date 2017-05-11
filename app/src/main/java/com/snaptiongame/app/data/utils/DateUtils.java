package com.snaptiongame.app.data.utils;

/**
 * @author Tyler Wong
 */

public class DateUtils {

    public static final long MILLIS = 1000;
    public static final long TWO_WEEKS_OFFSET = 1123200000;
    public static final long TWO_WEEKS = 1209600000;
    public static final long ONE_DAY = 86400000;
    public static final long SECONDS_IN_HOUR = 3600;
    public static final int DAY = 0;
    public static final int HOUR = 1;

    public static long getNow() {
        return System.currentTimeMillis() / MILLIS;
    }

    public static boolean isPastDate(long date1, long date2) {
        return date1 <= date2;
    }

    public static boolean isPastNow(long date) {
        return date <= getNow();
    }

    public static int[] getTimeRemaining(long date) {
        long dateDiff = date - getNow();
        long daySeconds = ONE_DAY / MILLIS;
        int[] timeArr = new int[2];

        if (dateDiff <= 0) {
            timeArr[0] = 0;
            timeArr[1] = DAY;
        }
        else if (dateDiff < daySeconds) {
            timeArr[0] = (int) Math.ceil(dateDiff / SECONDS_IN_HOUR);
            timeArr[1] = HOUR;
        }
        else {
            timeArr[0] = (int) Math.ceil(dateDiff / daySeconds);
            timeArr[1] = DAY;
        }
        return timeArr;
    }
}
