package com.snaptiongame.app.presentation.view.shop

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.snaptiongame.app.R
import com.snaptiongame.app.data.models.Offer

/**
 * @author Tyler Wong
 */
class OfferAdapter : RecyclerView.Adapter<OfferViewHolder>() {

    private var offers: MutableList<Offer> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): OfferViewHolder {
        val view = LayoutInflater.from(parent?.context)
                .inflate(R.layout.offer_card, parent, false)
        return OfferViewHolder(view)
    }

    override fun onBindViewHolder(holder: OfferViewHolder?, position: Int) {
        val offer: Offer = offers[position]
        holder?.name?.text = offer.name
        holder?.price?.text = offer.price.toString()
    }

    override fun getItemCount(): Int = offers.size

    fun addOffer(offer: Offer) {
        offers.add(offer)
        notifyItemInserted(offers.size)
    }
}