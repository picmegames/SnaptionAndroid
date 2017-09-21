package com.snaptiongame.app.presentation.view.utils

import android.content.Context
import android.content.res.Resources
import android.text.TextUtils

import com.snaptiongame.app.R
import com.snaptiongame.app.data.models.ActivityFeedItem
import com.snaptiongame.app.data.utils.DateUtils

/**
 * @author Tyler Wong
 */

object ActivityFeedUtils {

    const val FRIEND_MADE_GAME = 0
    const val FRIEND_INVITED_GAME = 1
    const val CAPTIONED_GAME = 2
    const val FRIENDED_YOU = 3
    const val NEW_FACEBOOK_FRIEND = 4

    @JvmStatic
    fun getMessage(context: Context, item: ActivityFeedItem): CharSequence {
        var message: CharSequence = ""
        val res = context.resources

        when (item.type) {
            FRIEND_MADE_GAME -> message = TextUtils.concat(TextStyleUtils.getTextBolded(item.friend.username), " ",
                    res.getString(R.string.friend_made_game))
            FRIEND_INVITED_GAME -> message = TextUtils.concat(TextStyleUtils.getTextBolded(item.friend.username), " ",
                    res.getString(R.string.friend_invited_game))
            CAPTIONED_GAME -> message = TextUtils.concat(TextStyleUtils.getTextBolded(item.friend.username), " ",
                    res.getString(R.string.captioned_game), " ", item.caption?.assocFitB?.beforeBlank,
                    TextStyleUtils.getTextUnderlined(item.caption?.caption ?: ""), item.caption?.assocFitB?.afterBlank)
            FRIENDED_YOU -> message = TextUtils.concat(TextStyleUtils.getTextBolded(item.friend.username), " ",
                    res.getString(R.string.friended_you))
            NEW_FACEBOOK_FRIEND -> message = TextUtils.concat(res.getString(R.string.new_facebook_friend_prefix), " ",
                    item.friend.fullName, " ", res.getString(R.string.new_facebook_friend_suffix),
                    " ", TextStyleUtils.getTextBolded(item.friend.username))
            else -> {
            }
        }

        if (!message.toString().endsWith(".")) {
            message = TextUtils.concat(message, ".")
        }

        message = TextUtils.concat(message, " ",
                TextStyleUtils.getTextGray(DateUtils.getTimeSince(context, item.date)))

        return message
    }
}
