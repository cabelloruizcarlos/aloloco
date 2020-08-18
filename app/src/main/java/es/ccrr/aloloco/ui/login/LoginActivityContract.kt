package es.ccrr.aloloco.ui.login

interface LoginActivityContract {

    interface LoginActivityView{
        fun setLayout(isLoginLayoutShown: Boolean)
        fun loadUrl(url: String)
        fun goToNextScreen()
    }

    interface LoginActivityPresenter{

        fun onClicked(button: Int)
    }

    interface LoginActivityModel{

        fun isLoginLayoutShown(): Boolean
        fun setIsLoginLayoutShown(value: Boolean)
    }
}