package es.ccrr.aloloco.ui.login.login_page

import android.os.Handler
import android.os.Looper

class LoginPageModel : LoginPageContract.LoginPageModel {

    private val handler: Handler = Handler(Looper.getMainLooper());
    private var mUserEmail = ""

    private var isLoginLayoutShown = false

    override fun setEmail(email: String?) {
        mUserEmail = email?: ""
    }

    override fun getEmail(): String {
        return mUserEmail
    }

    override fun getHandler(): Handler { return handler}

    override fun setIsLoginLayoutShown(value: Boolean){ isLoginLayoutShown = value}
    override fun isLoginLayoutShown() : Boolean{ return isLoginLayoutShown}

}
