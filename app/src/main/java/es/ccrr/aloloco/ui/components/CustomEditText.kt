package es.ccrr.aloloco.ui.components

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.InputType
import android.text.TextUtils
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import androidx.core.content.res.ResourcesCompat
import es.ccrr.aloloco.R
import kotlinx.android.synthetic.main.view_login_edit_text.view.*

/**
 * Created by carlos cabello ruiz on 18/08/2020.
 */

class CustomEditText @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null, defStyleAttr: Int = 0): RelativeLayout(context, attributeSet, defStyleAttr) {
    private val TAG = CustomEditText::class.java.simpleName
    private val DURATION: Long = 250

    private lateinit var inputType: String
    private var listener: CustomEditTextDialogCallback? = null
    private var mErrorShow = false
    private var isSmall = false

    init {
        LayoutInflater.from(context).inflate(R.layout.view_login_edit_text, this)

        var text = " "
        attributeSet?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.custom_view)

            try {
                text = typedArray.getString(R.styleable.custom_view_text)
                inputType = typedArray.getString(R.styleable.custom_view_inputType) ?: " "

            } finally {
                typedArray.recycle()
            }
        }

        setLabel(text)
        setInfo()
        setEditText()
    }


    private fun setLabel(text: String = " ") {

        customEditTextLabel.typeface = Typeface.createFromAsset(context.assets, "fonts/GillSans.ttf")
        customEditTextLabel.text = text
    }


    private fun setInfo() {
        when (inputType) {
            "date" -> {
                customEditTextInfo.text = context.getString(R.string.login_dob_info)
                customEditTextInfo.setTextColor(ResourcesCompat.getColor(resources, R.color.colorAccent, null))
                customEditTextInfo.visibility = INVISIBLE
            }
            "textEmailAddress" -> {
                customEditTextInfo.text = context.getString(R.string.login_email_info)
                customEditTextInfo.visibility = INVISIBLE
            }
            "textPassword" -> {
                customEditTextInfo.text = context.getString(R.string.login_password_info)
                customEditTextInfo.visibility = INVISIBLE
            }
            else -> {
                customEditTextInfo.text = context.getString(R.string.wrong_profile_input)
                customEditTextInfo.visibility = INVISIBLE
            }
        }
    }


    private fun setEditText() {
        customEditTextView.inputType = getInputTypeValue(inputType)

        if (inputType == "dialog") {

            customEditTextView.isFocusableInTouchMode = false
            customEditTextView.isFocusable = false
            customEditTextView.setOnClickListener{ listener?.showDialog() }
        } else {

            customEditTextView.setOnFocusChangeListener{ view, hasFocus ->

                if (view.id == R.id.customEditTextView && !hasFocus) {

                    if (inputType == "textPassword" && !mErrorShow)
                        customEditTextInfo.visibility = INVISIBLE
                    if (isSmall && TextUtils.isEmpty(customEditTextView.text))
                        makeItNormal()
                } else if (view.id == R.id.customEditTextView && hasFocus) {

                    if (inputType == "textPassword")
                        customEditTextInfo.visibility = VISIBLE
                    if (!isSmall)
                        makeItSmaller()
                }
            }
            customEditTextView.addTextChangedListener(object : TextWatcher {
                private var current = ""
                private val yyyymmdd = "yyyymmdd"

                override fun onTextChanged(charSequence: CharSequence, start: Int, before: Int, count: Int) {
                    hideWrongInput()

                    if (inputType == "date") if (charSequence.toString() != current) {
                        var clean = charSequence.toString().replace("[^\\d.]".toRegex(), "")
                        val cleanC = current.replace("[^\\d.]".toRegex(), "")
                        val cl = clean.length
                        var sel = cl
                        var i = 4
                        while (i <= cl && i < 8) {
                            sel++
                            i += 2
                        }

                        //Fix for pressing delete next to a forward slash`
                        if (clean == cleanC)
                            sel--

                        clean = if (clean.length < 8) {
                            clean + yyyymmdd.substring(clean.length)
                        } else {
                            val year = clean.substring(0, 4).toInt()
                            val mon = clean.substring(4, 6).toInt()
                            val day = clean.substring(6, 8).toInt()

                            String.format("%02d%02d%02d", year, if (mon > 12) 12 else mon, if (day > 31) 31 else day)
                        }
                        // Firebase BugFixing: StringIndexOutOfBoundsException - String.substring(StringIndexOutOfBoundsException: length=7; index=8 CustomEditText.java:166)
                        clean = if (clean.length != 8)
                            yyyymmdd
                        else
                            String.format("%s-%s-%s", clean.substring(0, 4), clean.substring(4, 6), clean.substring(6, 8))

                        sel = if (sel < 0) 0 else sel
                        current = clean
                        customEditTextView.setText(current)
                        customEditTextView.setSelection(if (sel < current.length) sel else current.length)
                    }
                }

                override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

                override fun afterTextChanged(editable: Editable) {}
            })
        }
    }


    fun setDialogListener(pListener: CustomEditTextDialogCallback) {
        listener = pListener
    }


    /* Set layout */
    fun showWrongInput() {
        if (inputType == "textPassword")
            customEditTextInfo.text = context.getString(R.string.login_password_err_info)

        customEditTextInfo.setTextColor(ResourcesCompat.getColor(resources, android.R.color.holo_red_dark, null))
        showRightIcon()
        customEditTextInfo.visibility = VISIBLE
        mErrorShow = true
    }


    fun hideWrongInput() {
        if (mErrorShow) {
            if (inputType.equals("textPassword")) {
                customEditTextInfo.setTextColor(ResourcesCompat.getColor(resources, android.R.color.white, null))
                customEditTextInfo.text = context.getString(R.string.login_password_info)
            } else {
                customEditTextInfo.visibility = INVISIBLE
            }
            hideRightIcon()
            mErrorShow = false
        }
    }


    private fun showRightIcon() {
        val iconWidth = resources.getDimension(R.dimen.icon_error_width).toInt()
        val iconHeight = resources.getDimension(R.dimen.icon_error_height).toInt()
        val dr = ResourcesCompat.getDrawable(resources, R.drawable.icon_error, null)
        val bitmap = (dr as BitmapDrawable?)!!.bitmap
        val icon: Drawable = BitmapDrawable(resources, Bitmap.createScaledBitmap(bitmap, iconWidth, iconHeight, true))

        customEditTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, icon, null)
    }


    private fun hideRightIcon() {
        customEditTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
    }


    private fun makeItSmaller() {
        customEditTextLabel.animate().y(-5f).setDuration(DURATION).start()
        customEditTextLabel.animate().x(10f).setDuration(DURATION).start()
        customEditTextLabel.animate().scaleYBy(-0.3f).setDuration(DURATION).start()
        customEditTextLabel.animate().scaleXBy(-0.3f).setDuration(DURATION).start()
        isSmall = true
    }


    private fun makeItNormal() {
        customEditTextLabel.animate().x(35f).setDuration(DURATION).start()
        customEditTextLabel.animate().y(60f).setDuration(DURATION).start()
        customEditTextLabel.animate().scaleYBy(0.3f).setDuration(DURATION).start()
        customEditTextLabel.animate().scaleXBy(0.3f).setDuration(DURATION).start()
        isSmall = false
    }


    /* Getters & Setters */
    fun getText(): String {
        return customEditTextView.text.toString()
    }

    fun setText(text: String) {
        customEditTextView.setText(text)
    }


    /* Util */
    private fun getInputTypeValue(inputType: String): Int {

        return when (inputType) {
            "date" -> InputType.TYPE_CLASS_NUMBER
            "textEmailAddress" -> InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
            "textPassword" -> InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            "dialog" -> InputType.TYPE_NULL
            else -> InputType.TYPE_CLASS_TEXT
        }
    }

    interface CustomEditTextDialogCallback{
        fun showDialog()
    }
}