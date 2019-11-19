package es.ccrr.aloloco.ui.Views

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import es.ccrr.aloloco.R

class CustomTextView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attributeSet, defStyleAttr) {

    init {
        attributeSet?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.custom_view)

            try {
                typeface = when (typedArray.getString(R.styleable.custom_view_custom_font)) {
                    "medium" -> Typeface.DEFAULT_BOLD
                    "regular" -> Typeface.DEFAULT
                    else -> Typeface.DEFAULT_BOLD
                }
            } finally {
                typedArray.recycle()
            }
        }
    }
}