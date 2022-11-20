package com.android.simpleweather.logic.network

import com.android.simpleweather.logic.entity.Forecast
import com.android.simpleweather.logic.entity.RealTime
import com.android.simpleweather.logic.entity.RealTimeRes
import io.reactivex.rxjava3.core.Observable
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface WeatherServer {
    @GET("v2.5/${Token.token}/{lng},{lat}/realtime.json")
    fun getRealTimeWeather(@Path("lat") lat:Double,@Path("lng") lng:Double):Observable<RealTime>

    @GET("v2.5/${Token.token}/{lng},{lat}/forecast.json")
    fun getForecast(@Path("lat") lat:Double,@Path("lng") lng:Double):Observable<Forecast>
}