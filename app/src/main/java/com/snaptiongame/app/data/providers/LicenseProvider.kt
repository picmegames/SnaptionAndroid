@file:JvmName("LicenseProvider")

package com.snaptiongame.app.data.providers

import android.content.Context
import com.snaptiongame.app.R
import com.snaptiongame.app.data.models.License
import io.reactivex.Single

/**
 * @author Tyler Wong
 */

fun getLicenses(context: Context): Single<List<License>> {
    return Single.defer { Single.just(parseLicenses(context)) }
}

private fun parseLicenses(context: Context): List<License> {
    var licenses: MutableList<License> = mutableListOf()
    var names: Array<String> = context.resources.getStringArray(R.array.license_names)
    var authors: Array<String> = context.resources.getStringArray(R.array.license_authors)

    names.forEachIndexed { index, s -> licenses.add(License(s, authors[index])) }

    return licenses
}
