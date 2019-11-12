package es.ccrr.aloloco.ui.base.mvp

import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import es.ccrr.aloloco.ui.base.BaseFrameActivity

public abstract class MvpActivity<Presenter : BasePresenter<BaseView>> : BaseFrameActivity<Fragment>(), BaseView {

    private var presenter: Presenter? = null

    protected abstract fun createPresenter(): Presenter

    fun getPresenter(): Presenter? {
        return presenter
    }

    override fun setContentView(@LayoutRes layoutResID: Int) {
        super.setContentView(layoutResID)
        this.presenter = createPresenter()
        presenter?.onCreate()
    }

    override fun onStart() {
        super.onStart()
        presenter?.onStart()
    }

    override fun onResume() {
        super.onResume()
        presenter?.onResume()
    }

    override fun onPause() {
        super.onPause()
        presenter?.onPause()
    }

    override fun onStop() {
        super.onStop()
        presenter?.onStop()
    }

    override protected fun onDestroy() {
        super.onDestroy()
        presenter?.onDestroy()
    }
}