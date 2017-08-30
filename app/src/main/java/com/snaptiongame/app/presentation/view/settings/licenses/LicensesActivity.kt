package com.snaptiongame.app.presentation.view.settings.licenses

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import com.snaptiongame.app.R
import com.snaptiongame.app.data.models.License
import kotlinx.android.synthetic.main.activity_licenses.*

/**
 * @author Tyler Wong
 */
class LicensesActivity : AppCompatActivity(), LicensesContract.View {

    private var licensePresenter: LicensesContract.Presenter = LicensesPresenter(this)
    private val adapter: LicensesAdapter = LicensesAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_licenses)

        setSupportActionBar(this.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.licenses)

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
}
