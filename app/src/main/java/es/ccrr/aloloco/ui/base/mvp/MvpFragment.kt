package es.ccrr.aloloco.ui.base.mvp

import android.os.Bundle
import android.view.View
import es.ccrr.aloloco.ui.base.BaseFragment

public abstract class MvpFragment<Presenter: BasePresenter<BaseView>>: BaseFragment(), BaseView {

    private var presenter: Presenter? = null

    protected abstract fun createPresenter(): Presenter

    fun getPresenter(): Presenter? {
        return presenter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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

    override fun onDestroyView() {
        super.onDestroyView()
        presenter?.onDestroy()
    }
}