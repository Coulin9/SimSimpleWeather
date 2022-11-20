package com.android.simpleweather.logic.entity

import com.google.gson.annotations.SerializedName
import java.util.*

data class Forecast(val status:String,@SerializedName("result")val forecastRes:ForecastRes?)

data class ForecastRes(val hourly:Hourly,val daily:Daily)

data class Hourly(val status:String,val temperature:List<HourlyValue>,val skycon:List<HourlyValue>)

data class HourlyValue(val datetime:Date,val value:String)

data class Daily(val status: String,val temperature: List<DailyTempValue>,val skycon:List<DailySkyconValue>)

data class DailyTempValue(val date:Date,val max:Float,val min:Float,val avg:Float)

data class DailySkyconValue(val date:Date,val value: String)