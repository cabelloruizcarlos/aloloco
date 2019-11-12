package es.ccrr.aloloco.ui.base.mvp

import es.ccrr.aloloco.util.AlertDialogCallback

interface BaseView {

    abstract fun showProgressDialog()

    abstract fun dismissProgressDialog()

    abstract fun launchBrowser(url: String)

    abstract fun showToast(message: String)

    abstract fun showAlert(
        title: String,
        message: String,
        firstButtonCopy: String,
        secondButtonCopy: String,
        forceAnswer: Boolean,
        firstCallback: AlertDialogCallback,
        secondaryCallback: AlertDialogCallback
    )
}
