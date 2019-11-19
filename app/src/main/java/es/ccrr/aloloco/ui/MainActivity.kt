package es.ccrr.aloloco.ui

import android.os.Bundle
import es.ccrr.aloloco.R
import es.ccrr.aloloco.ui.base.BaseActivity

class MainActivity : BaseActivity(), MainActivityContract.MainActivityView {

    private var presenter: MainActivityPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        presenter = MainActivityPresenter(this)
    }
}