package com.snaptiongame.app.presentation.view.utils;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;

import com.snaptiongame.app.R;
import com.snaptiongame.app.data.models.ActivityFeedItem;
import com.snaptiongame.app.data.utils.DateUtils;

/**
 * @author Tyler Wong
 */

public class ActivityFeedUtils {

    public static final int FRIEND_MADE_GAME = 0;
    public static final int FRIEND_INVITED_GAME = 1;
    public static final int CAPTIONED_GAME = 2;
    public static final int FRIENDED_YOU = 3;
    public static final int NEW_FACEBOOK_FRIEND = 4;

    public static CharSequence getMessage(Context context, ActivityFeedItem item) {
        CharSequence message = "";
        Resources res = context.getResources();

        switch (item.getType()) {
            case FRIEND_MADE_GAME:
                message = TextUtils.concat(TextStyleUtils.getTextBolded(item.getFriend().getUsername()), " ",
                        res.getString(R.string.friend_made_game));
                break;
            case FRIEND_INVITED_GAME:
                message = TextUtils.concat(TextStyleUtils.getTextBolded(item.getFriend().getUsername()), " ",
                        res.getString(R.string.friend_invited_game));
                break;
            case CAPTIONED_GAME:
                message = TextUtils.concat(TextStyleUtils.getTextBolded(item.getFriend().getUsername()), " ",
                        res.getString(R.string.captioned_game), " ", item.getCaption().getAssocFitB().getBeforeBlank(),
                        TextStyleUtils.getTextUnderlined(item.getCaption().getCaption()), item.getCaption().getAssocFitB().getAfterBlank());
                break;
            case FRIENDED_YOU:
                message = TextUtils.concat(TextStyleUtils.getTextBolded(item.getFriend().getUsername()), " ",
                        res.getString(R.string.friended_you));
                break;
            case NEW_FACEBOOK_FRIEND:
                message = TextUtils.concat(res.getString(R.string.new_facebook_friend_prefix), " ",
                        item.getFriend().getFullName(), " ", res.getString(R.string.new_facebook_friend_suffix),
                        " ", TextStyleUtils.getTextBolded(item.getFriend().getUsername()));
                break;
            default:
                break;
        }

        if (!message.toString().endsWith(".")) {
            message = TextUtils.concat(message, ".");
        }

        message = TextUtils.concat(message, " ",
                TextStyleUtils.getTextGray(DateUtils.INSTANCE.getTimeSince(context, item.getDate())));

        return message;
    }
}
