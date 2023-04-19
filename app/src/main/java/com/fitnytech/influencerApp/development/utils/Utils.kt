package com.fitnytech.influencerApp.development.utils

import android.content.Context
import android.widget.Toast


object Utils {
     fun showToast(mContext: Context?, message: String?) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()
    }
}