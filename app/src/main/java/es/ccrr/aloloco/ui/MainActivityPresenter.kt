package es.ccrr.aloloco.ui

class MainActivityPresenter(view: MainActivityContract.MainActivityView):  MainActivityContract.MainActivityPresenter {

    private var mainActiviyView: MainActivityContract.MainActivityView = view
    private var mainActivityModel: MainActivityContract.MainActivityModel = MainActivityModel()

    init {
    }
}