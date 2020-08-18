package es.ccrr.aloloco.ui.login

class LoginActivityModel: LoginActivityContract.LoginActivityModel {

    private var isLoginLayoutShown = false

    override fun setIsLoginLayoutShown(value: Boolean){ isLoginLayoutShown = value}

    override fun isLoginLayoutShown() : Boolean{ return isLoginLayoutShown}
}