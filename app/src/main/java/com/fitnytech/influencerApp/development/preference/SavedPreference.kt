package com.fitnytech.influencerApp.development.preference

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.fitnytech.influencerApp.development.ui.activity.LoginActivity

object SavedPreference {

    private const val EMAIL= "email"
    private const val USERNAME="username"
    private const val USERID="id"
    private const val INFLUENCER_CODE="cod"

    private  fun getSharedPreference(ctx: Context?): SharedPreferences? {
        return PreferenceManager.getDefaultSharedPreferences(ctx)
    }

    private fun  editor(context: Context, const:String, string: String){
        getSharedPreference(
            context
        )?.edit()?.putString(const,string)?.apply()
    }

    fun getEmail(context: Context)= getSharedPreference(
        context
    )?.getString(EMAIL,"")

    fun getCode(context: Context)= getSharedPreference(
        context
    )?.getString(INFLUENCER_CODE,"")

    fun getUserId(context: Context)= getSharedPreference(
        context
    )?.getString(USERID,"")

    fun setEmail(context: LoginActivity, email: String){
        editor(
            context,
            EMAIL,
            email
        )
    }

    fun setInfluencerCode(context: Context, code: String){
        editor(
            context,
            INFLUENCER_CODE,
            code
        )
    }

    fun setUserId(context: Context, userId: String){
        editor(
            context,
            USERID,
            userId
        )
    }


    fun setUsername(context: LoginActivity, username:String){
        editor(
            context,
            USERNAME,
            username
        )
    }

    fun getUsername(context: Context) = getSharedPreference(
        context
    )?.getString(USERNAME,"")

}