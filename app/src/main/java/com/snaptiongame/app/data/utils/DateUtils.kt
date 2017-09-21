package com.snaptiongame.app.data.utils

import android.content.Context
import android.content.res.Resources

import com.snaptiongame.app.R

/**
 * @author Tyler Wong
 */

object DateUtils {

    const val TWO_WEEKS_DAYS = 14
    const val MILLIS: Long = 1000
    const val TWO_WEEKS: Long = 1209600000

    private const val ONE_DAY: Long = 86400000
    private const val SECONDS_IN_HOUR: Long = 3600
    private const val SECONDS_IN_MINUTE = 60
    private const val MINUTES_IN_HOUR = 60
    private const val HOURS_IN_DAY = 24

    @JvmStatic
    val now: Long
        get() = System.currentTimeMillis() / MILLIS

    @JvmStatic
    fun isPastDate(date1: Long, date2: Long): Boolean {
        return date1 <= date2
    }

    @JvmStatic
    fun isPastNow(date: Long): Boolean {
        return date <= now
    }

    @JvmStatic
    fun getTimeSince(context: Context, date: Long): String {
        val diffInDays: Int
        val diff = now - date
        val res = context.resources

        diffInDays = (diff / (SECONDS_IN_MINUTE * MINUTES_IN_HOUR * HOURS_IN_DAY)).toInt()
        if (diffInDays > 0) {
            return String.format(res.getString(R.string.days), diffInDays)
        }
        else {
            val diffHours = (diff / (SECONDS_IN_MINUTE * MINUTES_IN_HOUR)).toInt()
            if (diffHours > 0) {
                return String.format(res.getString(R.string.hours), diffHours)
            }
            else {
                val diffMinutes = (diff / SECONDS_IN_MINUTE % MINUTES_IN_HOUR).toInt()
                return String.format(res.getString(R.string.minutes), diffMinutes)
            }
        }
    }

    @JvmStatic
    fun getTimeLeftLabel(context: Context, date: Long): String {
        val dateDiff = date - now
        val daySeconds = ONE_DAY / MILLIS
        var timeLeft = 0
        val res = context.resources

        if (dateDiff <= 0) {
            return res.getQuantityString(R.plurals.days_left, timeLeft, timeLeft)
        }
        else if (dateDiff < daySeconds) {
            timeLeft = Math.ceil((dateDiff / SECONDS_IN_HOUR).toDouble()).toInt()
            return res.getQuantityString(R.plurals.hours_left, timeLeft, timeLeft)
        }
        else {
            timeLeft = Math.ceil((dateDiff / daySeconds).toDouble()).toInt()
            return res.getQuantityString(R.plurals.days_left, timeLeft, timeLeft)
        }
    }
}
