package com.fitnytech.influencerApp.development.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.res.ResourcesCompat
import com.fitnytech.influencerApp.development.R


class CustomProgressDialog {
    lateinit var dialog: CustomDialog


    @SuppressLint("MissingInflatedId")
    fun show(context: Context, title: CharSequence?): Dialog {
        val inflater = (context as Activity).layoutInflater
        val view = inflater.inflate(R.layout.custom_progress_dialog, null)
        val tvTitle = view.findViewById<TextView>(R.id.tvTitle)
        val loadingCardView = view.findViewById<CardView>(R.id.loadingCardView)
        val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)
        if (title != null) {
            tvTitle.text = title
        }

        loadingCardView.setCardBackgroundColor(Color.parseColor("#70000000"))
        setColorFilter(
            progressBar.indeterminateDrawable, ResourcesCompat.getColor(
                context.resources,
                R.color.colorPrimary,
                null
            )
        )
        tvTitle.setTextColor(Color.WHITE)

        dialog = CustomDialog(context)
        dialog.setContentView(view)
        dialog.show()
        return dialog
    }

    private fun setColorFilter(drawable: Drawable, color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            drawable.colorFilter = BlendModeColorFilter(color, BlendMode.SRC_ATOP)
        } else {
            @Suppress("DEPRECATION")
            drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
        }
    }

    class CustomDialog(context: Context) : Dialog(context, R.style.CustomDialogTheme) {
        init {
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            window?.decorView?.setOnApplyWindowInsetsListener { _, insets ->
                insets.consumeSystemWindowInsets()
            }
        }
    }
}