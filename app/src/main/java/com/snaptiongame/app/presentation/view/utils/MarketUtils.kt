@file:JvmName("MarketUtils")

package com.snaptiongame.app.presentation.view.utils

import android.content.Context
import android.content.Intent
import android.content.ActivityNotFoundException
import android.net.Uri
import com.snaptiongame.app.R

/**
 * @author Tyler Wong
 */
fun goToListing(context: Context) {
    val uri = Uri.parse(String.format(context.getString(R.string.market_uri), context.packageName))
    val goToMarket = Intent(Intent.ACTION_VIEW, uri)

    try {
        context.startActivity(goToMarket)
    }
    catch (e: ActivityNotFoundException) {
        context.startActivity(Intent(Intent.ACTION_VIEW,
                Uri.parse(context.getString(R.string.store_url))))
    }
}
