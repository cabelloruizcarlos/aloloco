package es.ccrr.aloloco.ui.base

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import es.ccrr.aloloco.util.AlertDialogCallback
import es.ccrr.aloloco.util.DialogUtil
import es.ccrr.aloloco.util.Util

open class BaseActivity: AppCompatActivity() {

    //TODO: Add a spinner to be use along the app

    override fun onDestroy() {
        super.onDestroy()
        Util.freeMemory()
    }

    fun showToast(message: String){
        DialogUtil.showToast(this, message)
    }

    fun showAlert(title: String, message: String, primaryBtn: String, secondaryBtn: String, forceAnswer: Boolean, callback: AlertDialogCallback?, secondaryCallback: AlertDialogCallback?) {
        DialogUtil.showAlert(this, title, message, primaryBtn, secondaryBtn, forceAnswer, callback, secondaryCallback)
    }

    fun launchBrowser(url: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }
}