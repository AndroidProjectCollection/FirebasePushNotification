package com.febrian.firebasenotification.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var uid : String? = null,
    var username : String? = null,
    var email : String? = null,
    var password : String? = null,
    var token : String? = null
) : Parcelable
