package com.fitnytech.influencerApp.development.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(
    val userName: String? = null,
    val userEmailId: String? = null,
    val userPhoneNumber: String? = null,
    val userProfilePicture: String? = null,
    val token: String? = null
) {
    data class Influencer(
        val influencerProfilePicture
        : String? = null
    )

}