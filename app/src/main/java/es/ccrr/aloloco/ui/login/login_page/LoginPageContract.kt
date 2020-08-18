package es.ccrr.aloloco.ui.login.login_page

import android.os.Handler
import android.view.WindowManager

interface LoginPageContract {

    interface LoginPageView{
        fun getWindowManager(): WindowManager?
    }

    interface LoginPagePresenter{
        fun getScreenWidth(): Int
        fun isLoginLayoutShown(): Boolean
        fun getEmail(): String
        fun setEmail(email: String)
        fun isValid(email: String, password: String): Boolean
    }

    interface LoginPageModel{

        fun setIsLoginLayoutShown(isLoginLayoutShown: Boolean)
        fun isLoginLayoutShown(): Boolean
        fun getHandler(): Handler
        fun setEmail(email: String?)
        fun getEmail(): String
    }
}