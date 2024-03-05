package at.fhv.mme.bt_dementia_app.utils

import android.content.Context
import android.util.TypedValue
import android.widget.CheckBox
import androidx.core.content.ContextCompat
import androidx.core.widget.CompoundButtonCompat
import at.fhv.mme.bt_dementia_app.R
import at.fhv.mme.bt_dementia_app.utils.Converters.dpToPx
import at.fhv.mme.bt_dementia_app.utils.Converters.spToPx

object CheckBoxUtils {
    fun setCheckbox(context: Context, checkBox: CheckBox, isDone: Boolean) {
        val paddingStart = 8.dpToPx()
        val paddingEnd = 8.dpToPx()
        checkBox.setPaddingRelative(paddingStart, 0, paddingEnd, 0)

        val textSize = 18.spToPx().toFloat()
        checkBox.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)

        if (isDone) {
            checkBox.isChecked = true
            checkBox.text = context.getString(R.string.checkbox_done)

            val textColor = ContextCompat.getColor(context, R.color.secondaryTextColor)
            checkBox.setTextColor(textColor)

            val buttonTint = ContextCompat.getColorStateList(context, R.color.secondaryTextColor)
            CompoundButtonCompat.setButtonTintList(checkBox, buttonTint)
        } else {
            checkBox.isChecked = false
            checkBox.text = context.getString(R.string.checkbox_not_done_yet)

            val textColor = ContextCompat.getColor(context, R.color.accentColor)
            checkBox.setTextColor(textColor)

            val buttonTint = ContextCompat.getColorStateList(context, R.color.accentColor)
            CompoundButtonCompat.setButtonTintList(checkBox, buttonTint)
        }
    }
}