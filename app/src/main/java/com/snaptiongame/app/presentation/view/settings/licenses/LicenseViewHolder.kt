package com.snaptiongame.app.presentation.view.settings.licenses

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.afollestad.materialdialogs.MaterialDialog
import com.snaptiongame.app.R
import kotlinx.android.synthetic.main.license_card.view.*

/**
 * @author Tyler Wong
 */
class LicenseViewHolder(itemView: View, licensesClickable: Boolean) : RecyclerView.ViewHolder(itemView) {
    val name: TextView = itemView.licenseName
    val author: TextView = itemView.licenseAuthor

    var content: String = ""

    init {
        if (licensesClickable) {
            itemView.setOnClickListener {
                MaterialDialog.Builder(itemView.context)
                        .title(name.text)
                        .content(content)
                        .positiveText(R.string.ok)
                        .show()
            }
        }
    }
}
