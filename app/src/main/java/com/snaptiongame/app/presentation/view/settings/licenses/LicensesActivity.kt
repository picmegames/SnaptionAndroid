package com.snaptiongame.app.presentation.view.settings.licenses

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.snaptiongame.app.R
import com.snaptiongame.app.data.models.License
import kotlinx.android.synthetic.main.activity_licenses.*
import java.util.HashMap

/**
 * @author Tyler Wong
 */
class LicensesActivity : AppCompatActivity(), LicensesContract.View {

    private var licensePresenter: LicensesContract.Presenter = LicensesPresenter(this)
    private val adapter: LicensesAdapter = LicensesAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_licenses)

        val remoteConfig = FirebaseRemoteConfig.getInstance()
        val defaults = HashMap<String, Any>()
        defaults.put(LICENSES_CLICKABLE, false)
        remoteConfig.setDefaults(defaults)
        remoteConfig.fetch()
        remoteConfig.activateFetched()

        setSupportActionBar(this.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.licenses)

        adapter.licensesClickable = remoteConfig.getBoolean(LICENSES_CLICKABLE)
        this.licenses.layoutManager = LinearLayoutManager(this)
        this.licenses.adapter = adapter
        licensePresenter.subscribe()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        android.R.id.home -> consume { super.onBackPressed() }
        else -> super.onOptionsItemSelected(item)
    }

    override fun setPresenter(presenter: LicensesContract.Presenter) {
        this.licensePresenter = presenter
    }

    override fun showLicenses(licenses: List<License>) {
        adapter.setLicenses(licenses)
    }

    override fun getContext(): Context {
        return this.applicationContext
    }

    fun consume(f: () -> Unit): Boolean {
        f()
        return true
    }

    companion object {
        private const val LICENSES_CLICKABLE = "licenses_clickable"
    }
}
