package com.snaptiongame.app.presentation.view.settings.licenses

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.snaptiongame.app.R
import com.snaptiongame.app.data.models.License

/**
 * @author Tyler Wong
 */
class LicensesAdapter : RecyclerView.Adapter<LicenseViewHolder>() {

    private var licenses: List<License> = listOf()
    internal var licensesClickable: Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): LicenseViewHolder {
        val view = LayoutInflater.from(parent?.context)
                .inflate(R.layout.license_card, parent, false)
        return LicenseViewHolder(view, licensesClickable)
    }

    override fun onBindViewHolder(holder: LicenseViewHolder, position: Int) {
        val license = licenses[position]
        holder.name.text = license.name
        holder.author.text = license.author
        holder.content = license.content
    }

    fun setLicenses(licenses: List<License>) {
        this.licenses = licenses
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = licenses.size
}
