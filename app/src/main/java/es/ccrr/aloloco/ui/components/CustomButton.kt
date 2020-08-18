package es.ccrr.aloloco.ui.components

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton
import es.ccrr.aloloco.R

class CustomButton @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null, defStyleAttr: Int = 0) : AppCompatButton(context, attributeSet, defStyleAttr) {

    init {
        val font = attributeSet?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.custom_view)

            try {
                typedArray.getString(R.styleable.custom_view_custom_font) ?: "GillSans-SemiBold.ttf"
            } finally {
                typedArray.recycle()
            }
        } ?: run { "GillSans-SemiBold.ttf" }

        typeface = Typeface.createFromAsset(context.assets, "fonts/$font")
        invalidate()
    }

    fun setTypeface(context: Context, typeface: String) {
        this.typeface = Typeface.createFromAsset(context.assets, "fonts/$typeface")
        invalidate()
    }
}