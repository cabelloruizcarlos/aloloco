package es.ccrr.aloloco.ui.login

import android.content.Intent
import es.ccrr.aloloco.R
import es.ccrr.aloloco.engine.util.Constants

class LoginActivityPresenter(private val view: LoginActivityContract.LoginActivityView, intent: Intent) :
    LoginActivityContract.LoginActivityPresenter {

    private var model: LoginActivityContract.LoginActivityModel = LoginActivityModel()

    init {
        model.setIsLoginLayoutShown(intent.getBooleanExtra(Constants.JOURNEY, false))

        view.setLayout(model.isLoginLayoutShown())
    }


    override fun onClicked(button: Int) {
        when (button) {
            R.id.mTerms -> view.loadUrl(Constants.TERMS_URL)
            R.id.mPolicy -> view.loadUrl(Constants.POLICY_URL)
//            R.id.mCloseBtn -> view.goToNextScreen()
        }
    }
}