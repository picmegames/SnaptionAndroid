package com.snaptiongame.app.presentation.view.shop;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

/**
 * @author Tyler Wong
 */

public final class ShopChecker {
    public static void shopCheck(FragmentActivity activity, ShopCheckCallback callback) {
        new MaterialDialog.Builder(activity)
                .onPositive((@NonNull MaterialDialog dialog, @NonNull DialogAction which) -> callback.confirmBuy())
                .title("Create a New Game")
                .content("Creating a game will cost you " + 5 + "coins, are you sure you want to continue?")
                .show();
    }
}
