package es.ccrr.aloloco.ui.base

import android.text.TextUtils
import androidx.fragment.app.Fragment
import es.ccrr.aloloco.util.AlertDialogCallback

open class BaseFragment: Fragment() {

    private fun existActivityObject(): Boolean {
        return activity as BaseActivity != null
    }

    fun launchBrowser(url: String) {
        if (existActivityObject() && TextUtils.isEmpty(url))
            (activity as BaseActivity).launchBrowser(url)
    }

    fun showToast(message: String) {
        // Firebase BugFixing: BaseFragment.java line 89 (Attempt to invoke virtual method 'void uk.co.beatone.mybeatone.ui.base.BaseActivity.showAlert([...][...])' on a null object reference)
        if (existActivityObject())
            (activity as BaseActivity).showToast(message)
    }

    fun showAlert(title: String, message: String, primaryBtn: String, secondaryBtn: String, forceAnswer: Boolean, callback: AlertDialogCallback? = null, secondaryCallback: AlertDialogCallback? = null) {
        if (existActivityObject())
            (activity as BaseActivity).showAlert(title, message, primaryBtn, secondaryBtn, forceAnswer, callback, secondaryCallback)
    }
}