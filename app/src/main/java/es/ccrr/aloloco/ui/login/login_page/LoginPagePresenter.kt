package es.ccrr.aloloco.ui.login.login_page

import android.os.Bundle
import android.text.TextUtils
import es.ccrr.aloloco.ui.base.BaseActivity
import es.ccrr.aloloco.util.Constants
import es.ccrr.aloloco.util.Util

class LoginPagePresenter(private val view: LoginPageContract.LoginPageView,private val arguments: Bundle?):LoginPageContract.LoginPagePresenter {

    private var model: LoginPageContract.LoginPageModel = LoginPageModel()
    private val mUtil = Util()

    init {
        arguments?.let {fragmentBundle ->
            model.setIsLoginLayoutShown(fragmentBundle.getString("page") == Constants.LOGIN_TABS[1])
            model.setEmail(fragmentBundle.getString("email"))
        }
    }


    override fun isValid(email: String, password: String): Boolean {

        val isValid =when {
            TextUtils.isEmpty(email) -> false
            TextUtils.isEmpty(password) -> false
            password.length <= 8 -> false
            else -> true
        }

        if (isValid)
            model.setEmail(email)

        return isValid
    }


    fun doAPICall(userInput: Array<String>) {

        // validate the userInput data and if its wrong don't forget to call to dismissProgressDialog()

        if (!model.isLoginLayoutShown())
            doARegistration()
        else
            doALogin()
    }


    private fun doALogin() {
        // TODO: Do the login API call and don't forget to call to dismissProgressDialog()
    }

    private fun doARegistration() {
        // TODO: Do the registration API call and don't forget to call to dismissProgressDialog()
    }


    /* Getters & Setters */
    override fun isLoginLayoutShown(): Boolean {
        return model.isLoginLayoutShown()
    }


    override fun setEmail(email: String) {
        model.setEmail(email)
    }


    override fun getEmail(): String {
        return model.getEmail()
    }


    /* Util */
    override fun getScreenWidth(): Int {
        return view.getWindowManager()?.let {
            mUtil.getScreenWidth(it)
        } ?: run { 0 }
    }
}
