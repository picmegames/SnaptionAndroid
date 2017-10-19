package com.snaptiongame.app.presentation.view.shop

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.snaptiongame.app.R

/**
 * @author Tyler Wong
 */

class ShopFragment : Fragment(), ShopContract.View {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater?.inflate(R.layout.shop_fragment, container, false)
    }

    override fun setPresenter(presenter: ShopContract.Presenter) {

    }

    companion object {
        val TAG: String = ShopFragment::class.java.simpleName
        val instance: ShopFragment
            get() = ShopFragment()
    }
}
