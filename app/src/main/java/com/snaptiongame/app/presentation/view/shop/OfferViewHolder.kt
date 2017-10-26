package com.snaptiongame.app.presentation.view.shop

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.offer_card.view.*

/**
 * @author Tyler Wong
 */
class OfferViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val name: TextView = itemView.name
    val price: TextView = itemView.price
}