package at.fhv.mme.bt_dementia_app.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.widget.Button
import android.widget.TextView
import at.fhv.mme.bt_dementia_app.R

object DialogUtils {
    fun showConfirmationDialog(context: Context, title: String, message: String, onConfirm: () -> Unit) {
        val dialog = Dialog(context).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCancelable(false)
            setContentView(R.layout.confirmation_dialog)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        val tvDialogTitle = dialog.findViewById<TextView>(R.id.tvDialogTitle)
        val tvDialogMessage = dialog.findViewById<TextView>(R.id.tvDialogMessage)
        val btnConfirmationCancel = dialog.findViewById<Button>(R.id.btnConfirmationCancel)
        val btnConfirmationConfirm = dialog.findViewById<Button>(R.id.btnConfirmationConfirm)

        tvDialogTitle.text = title
        tvDialogMessage.text = message

        btnConfirmationCancel.setOnClickListener {
            dialog.dismiss()
        }

        btnConfirmationConfirm.text = title.split(" ")[0]
        btnConfirmationConfirm.setOnClickListener {
            dialog.dismiss()
            onConfirm()
        }

        dialog.show()
    }
}