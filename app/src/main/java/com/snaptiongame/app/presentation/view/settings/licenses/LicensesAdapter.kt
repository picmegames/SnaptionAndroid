package com.snaptiongame.app.presentation.view.settings.licenses

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

/**
 * @author Tyler Wong
 */
class LicensesAdapter : RecyclerView.Adapter<LicenseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): LicenseViewHolder {
        val view = LayoutInflater.from(parent!!.context)
                .inflate(0, parent, false)
        return LicenseViewHolder(view)
    }

    override fun onBindViewHolder(holder: LicenseViewHolder?, position: Int) {

    }

    override fun getItemCount(): Int {
        return 0;
    }
}