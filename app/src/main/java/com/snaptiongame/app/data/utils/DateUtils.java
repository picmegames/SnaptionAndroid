package com.snaptiongame.app.data.utils;

import android.content.Context;
import android.content.res.Resources;

import com.snaptiongame.app.R;

/**
 * @author Tyler Wong
 */

public class DateUtils {

    public static final long MILLIS = 1000;
    public static final long TWO_WEEKS_OFFSET = 1123200000;
    public static final long TWO_WEEKS = 1209600000;
    public static final long ONE_DAY = 86400000;
    public static final long SECONDS_IN_HOUR = 3600;
    public static final int TWO_WEEKS_DAYS = 14;
    public static final int SECONDS_IN_MINUTE = 60;
    public static final int MINUTES_IN_HOUR = 60;
    public static final int HOURS_IN_DAY = 24;

    public static long getNow() {
        return System.currentTimeMillis() / MILLIS;
    }

    public static boolean isPastDate(long date1, long date2) {
        return date1 <= date2;
    }

    public static boolean isPastNow(long date) {
        return date <= getNow();
    }

    public static String getTimeSince(Context context, long date) {
        int diffInDays;
        long diff = date - getNow();
        Resources res = context.getResources();

        diffInDays = (int) (diff / (MILLIS * SECONDS_IN_MINUTE * MINUTES_IN_HOUR * HOURS_IN_DAY));
        if (diffInDays > 0) {
            return String.format(res.getString(R.string.days), diffInDays);
        }
        else {
            int diffHours = (int) (diff / (MILLIS * SECONDS_IN_MINUTE * MINUTES_IN_HOUR));
            if (diffHours > 0) {
                return String.format(res.getString(R.string.hours), diffHours);
            }
            else {
                int diffMinutes = (int) ((diff / (SECONDS_IN_MINUTE * MILLIS) % MINUTES_IN_HOUR));
                return String.format(res.getString(R.string.minutes), diffMinutes);
            }
        }
    }

    public static String getTimeLeftLabel(Context context, long date) {
        long dateDiff = date - getNow();
        long daySeconds = ONE_DAY / MILLIS;
        int timeLeft = 0;
        Resources res = context.getResources();

        if (dateDiff <= 0) {
            return res.getQuantityString(R.plurals.days_left, timeLeft, timeLeft);
        }
        else if (dateDiff < daySeconds) {
            timeLeft = (int) Math.ceil(dateDiff / SECONDS_IN_HOUR);
            return res.getQuantityString(R.plurals.hours_left, timeLeft, timeLeft);
        }
        else {
            timeLeft = (int) Math.ceil(dateDiff / daySeconds);
            return res.getQuantityString(R.plurals.days_left, timeLeft, timeLeft);
        }
    }
}
