package es.ccrr.aloloco.util

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import es.ccrr.aloloco.ui.dialog.CustomAlertDialog

object DialogUtil {

    fun showToast(activity: AppCompatActivity?, message: String?, length: Int = Toast.LENGTH_LONG) {
        if (activity == null || message == null)
            return

        Toast.makeText(activity, message, length).show()
    }

    fun showAlert(activity: AppCompatActivity, title: String, message: String, firstButtonCopy: String, secondButtonCopy: String, forceAnswer: Boolean, firstCallback: AlertDialogCallback?, secondaryCallback: AlertDialogCallback?) {

        val dialog = CustomAlertDialog.newInstance(activity, title, message, firstButtonCopy, secondButtonCopy)

        dialog.setFirstButtonListener(firstCallback)
        dialog.setSecondaryButtonListener(secondaryCallback)

        dialog.setCancelable(!forceAnswer)

        // Firebase BugFixing: IllegalStateException - LooperScheduler$ScheduledAction.run(android.view.WindowManager$BadTokenException:
        // Unable to add window -- token android.os.BinderProxy@41b048b is not valid; is your activity running? VouchersPresenter.java:130)
        if (!activity.isFinishing)
            dialog.show()
    }
}