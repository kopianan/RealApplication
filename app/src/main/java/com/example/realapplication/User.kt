package com.example.realapplication

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class User(
    val uid :String,
    val name :String,
           val photo :String,
           val email :String):Parcelable  {

    constructor():this("","","","")


}