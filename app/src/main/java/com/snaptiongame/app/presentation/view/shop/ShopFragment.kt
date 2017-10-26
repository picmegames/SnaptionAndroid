package com.snaptiongame.app.presentation.view.shop

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.snaptiongame.app.R
import com.snaptiongame.app.R.id.*
import com.snaptiongame.app.data.models.Offer
import kotlinx.android.synthetic.main.shop_fragment.*

/**
 * @author Tyler Wong
 */

class ShopFragment : Fragment(), ShopContract.View {

    private var shopPresenter: ShopContract.Presenter = ShopPresenter(this)

    private val specialAdapter: OfferAdapter = OfferAdapter()
    private val softAdapter: OfferAdapter = OfferAdapter()
    private val hardAdapter: OfferAdapter = OfferAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.shop_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        refreshLayout.setOnRefreshListener(shopPresenter::subscribe)

        specialOffers.adapter = specialAdapter
        softOffers.adapter = softAdapter
        hardOffers.adapter = hardAdapter

        specialOffers.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        softOffers.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        hardOffers.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        specialOffers.isNestedScrollingEnabled = false
        softOffers.isNestedScrollingEnabled = false
        hardOffers.isNestedScrollingEnabled = false

        shopPresenter.subscribe()
    }

    override fun setPresenter(presenter: ShopContract.Presenter) {
        this.shopPresenter = presenter
    }

    override fun showSpecialOffer(offer: Offer) = specialAdapter.addOffer(offer)

    override fun showSoftOffer(offer: Offer) = softAdapter.addOffer(offer)

    override fun showHardOffer(offer: Offer) = hardAdapter.addOffer(offer)

    override fun onCompleted() {
        specialOffers.visibility = if (specialAdapter.itemCount == 0) View.GONE else View.VISIBLE
        softOffers.visibility = if (softAdapter.itemCount == 0) View.GONE else View.VISIBLE
        hardOffers.visibility = if (hardAdapter.itemCount == 0) View.GONE else View.VISIBLE
        refreshLayout.isRefreshing = false
    }

    companion object {
        val TAG: String = ShopFragment::class.java.simpleName
        val instance: ShopFragment
            get() = ShopFragment()
    }
}
