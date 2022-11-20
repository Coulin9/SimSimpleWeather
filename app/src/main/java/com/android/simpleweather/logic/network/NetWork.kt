package com.android.simpleweather.logic.network

import android.widget.Toast
import com.android.simpleweather.MainActivity
import com.android.simpleweather.logic.entity.Forecast
import com.android.simpleweather.logic.entity.Places
import com.android.simpleweather.logic.entity.RealTime
import com.android.simpleweather.utils.LogUtil
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import kotlin.math.ln

object NetWork {
    private const val baseURL="https://api.caiyunapp.com/"
    private val retrofit=Retrofit.Builder()
        .baseUrl(baseURL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .build()
    private val weatherServer: WeatherServer = retrofit.create(WeatherServer::class.java)
    private val placeServer:PlaceServer= retrofit.create(PlaceServer::class.java)

    fun getRealTimeWeather(lat:Double,lng:Double)= weatherServer.getRealTimeWeather(lat, lng)
        .doOnError {
            //一定要有错误处理机制！
            LogUtil.e("getRealTime",it.message!!)
            //Toast.makeText(MainActivity.context,"网络请求失败！",Toast.LENGTH_SHORT).show()
        }
        .onErrorReturn {
            return@onErrorReturn RealTime("failed",null)
        }

    fun getForecast(lat: Double,lng: Double)= weatherServer.getForecast(lat, lng)
        .doOnError {
            LogUtil.e("getForecast",it.message!!)
            //Toast.makeText(MainActivity.context,"网络请求失败！",Toast.LENGTH_SHORT).show()
        }
        .onErrorReturn {
            return@onErrorReturn Forecast("failed",null)
        }

    fun queryPlace(name:String)= placeServer.queryPlace(name)
        .doOnError {
            LogUtil.e("getPlaces",it.message!!)
            //Toast.makeText(MainActivity.context,"网络请求失败！",Toast.LENGTH_SHORT).show()
        }
        .onErrorReturn {
            return@onErrorReturn Places("failed",null,null)
        }
}