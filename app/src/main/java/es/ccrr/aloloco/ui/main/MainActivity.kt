package es.ccrr.aloloco.ui.main

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import es.ccrr.aloloco.R
import es.ccrr.aloloco.engine.util.DialogUtil
import es.ccrr.aloloco.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity(), MainActivityContract.MainActivityView {

    private var presenter: MainActivityPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        presenter = MainActivityPresenter(this)

        mainList.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        mainList.adapter = MainListAdapter()

        mainProfileBtn.setOnClickListener { DialogUtil.showAlert(this, applicationContext.getString(R.string.app_name), "Coming soon...","Ok","null",false, null,null) }
    }
}