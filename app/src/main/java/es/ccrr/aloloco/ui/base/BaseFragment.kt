package es.ccrr.aloloco.ui.base

import android.text.TextUtils
import androidx.fragment.app.Fragment
import es.ccrr.aloloco.engine.util.AlertDialogCallback

open class BaseFragment : Fragment() {

    fun launchBrowser(url: String) {

        activity?.let { baseActivity ->
            if (TextUtils.isEmpty(url))
                (baseActivity as BaseActivity).launchBrowser(url)
        }
    }

    fun showProgressDialog() {

        activity?.let { baseActivity ->
            (baseActivity as BaseActivity).showProgressDialog(baseActivity.applicationContext)
        }
    }

    fun dismissProgressDialog() {

        activity?.let { baseActivity ->
            (baseActivity as BaseActivity).dismissProgressDialog()
        }
    }

    fun showToast(message: String) {

        // Firebase BugFixing: BaseFragment.java line 89 (Attempt to invoke virtual method 'void uk.co.beatone.mybeatone.ui.base.BaseActivity.showAlert([...][...])' on a null object reference)
        activity?.let { baseActivity ->
            (baseActivity as BaseActivity).showToast(message)
        }
    }

    fun showAlert(title: String, message: String, primaryBtn: String, secondaryBtn: String, forceAnswer: Boolean, callback: AlertDialogCallback? = null, secondaryCallback: AlertDialogCallback? = null) {

        activity?.let { baseActivity ->
            (baseActivity as BaseActivity).showAlert(title, message, primaryBtn, secondaryBtn, forceAnswer, callback, secondaryCallback)
        }
    }
}