package com.android.simpleweather.logic.entity

import com.google.gson.annotations.SerializedName

data class Place(val id:String,val name:String,@SerializedName("formatted_address")val address:String
,val location: Location)
