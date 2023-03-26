package com.example.reflexionai.models.entities

import android.graphics.Bitmap

data class Profile(
    var name : String,
    var phoneNo : String,
    var address : String,
    var email: String,
)

object UserProfile {
    var profile: Profile? = null
}