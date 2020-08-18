package es.ccrr.aloloco.ui.base

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import es.ccrr.aloloco.util.AlertDialogCallback
import es.ccrr.aloloco.util.DialogUtil
import es.ccrr.aloloco.util.Util

open class BaseActivity : AppCompatActivity() {

    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        progressBar = ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal)
        progressBar.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        Util.freeMemory()
    }

    fun showProgressDialog(pContext: Context) {

        progressBar?.let { loadingSpinner ->
            loadingSpinner.visibility = View.VISIBLE
        } ?: run {
            progressBar = ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal)
            showProgressDialog(pContext)
        }
    }

    open fun dismissProgressDialog() {
        progressBar?.let { loadingSpinner ->
            loadingSpinner.visibility = View.GONE
        }
    }

    fun showToast(message: String) {
        DialogUtil.showToast(this, message)
    }

    fun showAlert(title: String, message: String, primaryBtn: String, secondaryBtn: String, forceAnswer: Boolean, callback: AlertDialogCallback?, secondaryCallback: AlertDialogCallback?) {
        DialogUtil.showAlert(this, title, message, primaryBtn, secondaryBtn, forceAnswer, callback, secondaryCallback)
    }

    fun launchBrowser(url: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }
}