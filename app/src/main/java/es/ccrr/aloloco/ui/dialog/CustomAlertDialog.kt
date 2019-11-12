package es.ccrr.aloloco.ui.dialog

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.LinearLayout
import androidx.core.content.res.ResourcesCompat
import es.ccrr.aloloco.R
import es.ccrr.aloloco.util.AlertDialogCallback
import kotlinx.android.synthetic.main.alert_dialog.*

class CustomAlertDialog(context: Context, args: Array<String>): Dialog(context) {

    private val mTitle = args[0]
    private val mInfo = args[1]
    private val mFirstButton = args[2]
    private val mSecondButton: String? = args[3]

    private var mFirstBtnCallback: AlertDialogCallback? = null
    private var mSecondBtnCallback: AlertDialogCallback? = null

    init {

        val backgroundColor = ResourcesCompat.getColor(context.resources, android.R.color.transparent, null)
        window!!.setBackgroundDrawable(ColorDrawable(backgroundColor))
        window!!.requestFeature(Window.FEATURE_NO_TITLE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.alert_dialog)

        alert_dialog_title.text = mTitle
        alert_dialog_info.text = mInfo
        alert_dialog_first_btn.text = mFirstButton.toUpperCase()

        mSecondButton?.let{
            alert_dialog_second_btn.text = mSecondButton.toUpperCase()
        } ?: run {
            alert_dialog_second_btn.visibility = View.GONE

            val layoutParams = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT)
            layoutParams.weight = 2f
            alert_dialog_first_btn.layoutParams = layoutParams
        }

        alert_dialog_first_btn.setOnClickListener {
            dismiss()

            mFirstBtnCallback?.callback()
        }
        alert_dialog_second_btn.setOnClickListener {
            dismiss()

            mSecondBtnCallback?.callback()
        }
    }

    fun setFirstButtonListener(listener: AlertDialogCallback?) {
        mFirstBtnCallback = listener
    }

    fun setSecondaryButtonListener(listener: AlertDialogCallback?) {
        mSecondBtnCallback = listener
    }

    companion object{

        fun newInstance(
            activity: Activity,
            title: String,
            message: String,
            firstButtonCopy: String,
            secondButtonCopy: String
        ): CustomAlertDialog {

            val data = arrayOf(title, message, firstButtonCopy, secondButtonCopy)
            return CustomAlertDialog(activity, data)
        }
    }
}