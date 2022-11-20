package com.android.simpleweather.logic.entity

data class AirQuality(val pm25:Int,val aqi:AQI,val description:Description)

data class AQI(val chn:Int,val usa:Int)

data class Description(val usa:String,val chn:String)
