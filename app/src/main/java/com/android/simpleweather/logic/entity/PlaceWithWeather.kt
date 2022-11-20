package com.android.simpleweather.logic.entity

import java.io.Serializable
import java.util.*

class PlaceWithWeather(val status:String,place:Place,realTime:RealTime?,forecast:Forecast?):Serializable {
    data class HourlyForecastItem(val dateTime: Date,val skycon:String,val temperature:Float)
    data class DailyForecastItem(val date:Date,val skycon: String,val maxTemp:Float,val minTemp:Float,
    val avgTemp:Float)

    val place get() = _place!!
    private var _place:Place?=null

    val realTimeSkycon get() = _realTimeSkycon
    private var _realTimeSkycon=""

    val realTimeAirQuality get() = _realTimeAirQuality
    private var _realTimeAirQuality:AirQuality?=null

    val hourlyForecast get() = _hourlyForecast
    private var _hourlyForecast:List<HourlyForecastItem>?=null

    val dailyForecast get() = _dailyForecast
    private var _dailyForecast:List<DailyForecastItem>?=null

    val dswrf get() = _dswrf
    private var _dswrf:Float=0f

    val wind get() = _wind
    private var _wind:Wind?=null

    val precipitation get() = _precipitation
    private var _precipitation:Precipitation?=null

    val appTemp get() = _appTemp
    private var _appTemp=0f

    val temperature get() = _temperature
    private var _temperature=0f

    val humidity get() = _humidity
    private var _humidity=0f

    val visibility get() = _visibility
    private var _visibility=0f

    val pressure get() = _pressure
    private var _pressure=0f

    init {
        _place=place
        if(realTime!=null&&forecast!=null){
            _realTimeSkycon=realTime.realtimeRes!!.realtime.skycon
            _realTimeAirQuality=realTime.realtimeRes.realtime.airQuality
            val hSkycon=forecast.forecastRes!!.hourly.skycon
            val hTemp=forecast.forecastRes.hourly.temperature
            val hForecast= mutableListOf<HourlyForecastItem>()
            val dTemp=forecast.forecastRes.daily.temperature
            val dSkycon=forecast.forecastRes.daily.skycon
            val dForecast= mutableListOf<DailyForecastItem>()
            for(i in 0 until hSkycon.size){
                hForecast.add(HourlyForecastItem(hSkycon[i].datetime,hSkycon[i].value,hTemp[i].value.toFloat()))
            }
            _hourlyForecast=hForecast
            for(i in 0 until dSkycon.size){
                dForecast.add(DailyForecastItem(dSkycon[i].date,dSkycon[i].value,dTemp[i].max,dTemp[i].min,dTemp[i].avg))
            }
            _dailyForecast=dForecast
            _dswrf=realTime.realtimeRes.realtime.dswrf
            _wind=realTime.realtimeRes.realtime.wind
            _precipitation=realTime.realtimeRes.realtime.precipitation
            _appTemp=realTime.realtimeRes.realtime.appTemperature
            _temperature=realTime.realtimeRes.realtime.temperature
            _humidity=realTime.realtimeRes.realtime.humidity
            _visibility=realTime.realtimeRes.realtime.visibility
            _pressure=realTime.realtimeRes.realtime.pressure
        }
    }

    override fun toString(): String {
        return "PlaceWithWeather(_place=$_place, _realTimeSkycon='$_realTimeSkycon', _realTimeAirQuality=$_realTimeAirQuality, _hourlyForecast=$_hourlyForecast, _dailyForecast=$_dailyForecast, _dswrf=$_dswrf, _wind=$_wind, _precipitation=$_precipitation, _appTemp=$_appTemp, _temperature=$_temperature, _humidity=$_humidity, _visibility=$_visibility, _pressure=$_pressure)"
    }


}