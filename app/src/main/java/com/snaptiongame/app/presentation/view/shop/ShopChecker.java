package com.snaptiongame.app.presentation.view.shop;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.snaptiongame.app.R;

/**
 * @author Tyler Wong
 */

public final class ShopChecker {
    public static void shopCheck(FragmentActivity activity, ShopCheckCallback callback, int title, int content, int cost) {
        new MaterialDialog.Builder(activity)
                .onPositive((@NonNull MaterialDialog dialog, @NonNull DialogAction which) -> callback.confirmBuy())
                .onNegative((@NonNull MaterialDialog dialog, @NonNull DialogAction which) -> callback.cancelBuy())
                .title(title)
                .content(String.format(activity.getString(content), cost))
                .positiveText(R.string.ok)
                .negativeText(R.string.cancel)
                .show();
    }
}
