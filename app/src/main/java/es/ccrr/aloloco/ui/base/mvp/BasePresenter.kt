package es.ccrr.aloloco.ui.base.mvp

public abstract class BasePresenter<View: BaseView>(val view: View) {

    fun onCreate() {}

    fun onStart() {}

    fun onResume() {}

    fun onPause() {}

    fun onStop() {}

    fun onDestroy() {}
}
