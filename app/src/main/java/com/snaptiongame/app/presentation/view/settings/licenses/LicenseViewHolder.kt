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
class LicenseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val name: TextView = itemView.license_name
    val author: TextView = itemView.license_author

    var content: String = ""

    init {
        itemView.setOnClickListener {
            MaterialDialog.Builder(itemView.context)
                    .title(name.text)
                    .content(content)
                    .positiveText(R.string.ok)
                    .show()
        }
    }
}
