package com.android.simpleweather.ui.weather

import android.animation.ObjectAnimator
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.simpleweather.MainActivity
import com.android.simpleweather.databinding.WeatherInfoLayoutBinding
import com.android.simpleweather.logic.entity.PlaceWithWeather
import com.android.simpleweather.utils.*

class WeatherInfoFragment(private var data: PlaceWithWeather?):Fragment() {
    //这个Fragment是ViewPager中用来展示天气信息的Fragment
    //在这个Fragment中定义一个方法来绑定数据。
    private var binding:WeatherInfoLayoutBinding?=null

    private var adapter1:HourlyForecastAdapter?=null

    private var adapter2:DailyForecastAdapter?=null

    private val MIN_PRESSURE=86400f

    private val MAX_PRESSURE=107900f

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=WeatherInfoLayoutBinding.inflate(inflater,container,false)
        bindData(data!!)
        return binding?.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        data=null
        super.onActivityCreated(savedInstanceState)
    }

    override fun onDestroyView() {
        binding=null
        super.onDestroyView()
    }

    private fun bindData(data:PlaceWithWeather){
        val blurRadius=30f

        //空气质量
        //设置卡片的高斯模糊效果，注意parent是你想要模糊化的那个背景，这里就是WeatherFragment.blurRoot
        //setBlur(MainActivity.context,binding?.aqiBlur!!,blurRadius,WeatherFragment.blurRoot)
        if(data.realTimeAirQuality!=null){
            binding?.numAndDis?.text="${data.realTimeAirQuality?.aqi?.chn} - ${data.realTimeAirQuality?.description?.chn}"
            binding?.detailedDis?.text="当前AQI(CN)为${data.realTimeAirQuality?.aqi?.chn}。"
        }
        //LogUtil.d("bindData",data.toString())
        //小时级天气预报
        //setBlur(MainActivity.context,binding?.hourlyForecastBlur!!,blurRadius,WeatherFragment.blurRoot)
        if(data.hourlyForecast!=null){
            adapter1=HourlyForecastAdapter(data.hourlyForecast!!)
            binding?.hourlyForecastList?.adapter=adapter1
            binding?.hourlyForecastList?.layoutManager=LinearLayoutManager(MainActivity.context,LinearLayoutManager.HORIZONTAL,false)
        }
        //天级别天气预报
        //setBlur(MainActivity.context,binding?.dailyForecastBlur!!,blurRadius,WeatherFragment.blurRoot)
        if(data.dailyForecast!=null){
            adapter2= DailyForecastAdapter(data.dailyForecast!!)
            binding?.dailyForecastList?.adapter=adapter2
            binding?.dailyForecastList?.layoutManager=LinearLayoutManager(MainActivity.context)
        }
        //向下短波通量
        //setBlur(MainActivity.context,binding?.dswrfBlur!!,blurRadius,WeatherFragment.blurRoot)
        binding?.dswrfValue?.text="${data.dswrf.toInt()}"
        //风速风向
        //setBlur(MainActivity.context,binding?.windBlur!!,blurRadius,WeatherFragment.blurRoot)
        if(data.wind!=null){
            val direction=if(data.wind?.direction!!>180F) data.wind?.direction!!-180F else data.wind?.direction!!+180F
            ObjectAnimator.ofFloat(binding?.windDirectImage,"rotation",direction!!)
                .setDuration(500)
                .start()
            binding?.windDis?.text="${directionToDisc(data.wind?.direction!!)} - ${data?.wind?.speed?.toInt()!!}千米/时"
        }
        //体感温度
        //setBlur(MainActivity.context,binding?.appTempBlur!!,blurRadius,WeatherFragment.blurRoot)
        binding?.appTempValue?.text="${data.appTemp}°"
        //湿度
        //setBlur(MainActivity.context,binding?.humidityBlur!!,blurRadius,WeatherFragment.blurRoot)
        binding?.humidityValue?.text="${(data.humidity*100).toInt()}%"
        binding?.humidityDis?.text="当前露点温度为${calculateDropTemp(data.temperature,data.humidity*100).toInt()}°。"
        //能见度
        //setBlur(MainActivity.context,binding?.visibilityBlur!!,20f,WeatherFragment.blurRoot)
        binding?.visibilityValue?.text="${data.visibility.toInt()}公里"
        binding?.visibilityDis?.text="目前${getVisibilityDisc(data.visibility)}。"
        //气压
        //setBlur(MainActivity.context,binding?.pressureBlur!!,blurRadius,WeatherFragment.blurRoot)
        if(data.pressure!=0f){
            binding?.pressureValue?.text="${(data.pressure/100).toInt().toFloat()/10}千帕"
            val movePxs= ((156-8).toFloat() *((data.pressure-MIN_PRESSURE)/(MAX_PRESSURE-MIN_PRESSURE)))*(getDpi(MainActivity.context)/160)
            ObjectAnimator.ofFloat(binding?.pressureArrow!!,"translationX",movePxs)
                .setDuration(500)
                .start()
        }
        //实时天气
        //setBlur(MainActivity.context,binding?.realTimeBlur!!,blurRadius,WeatherFragment.blurRoot)
        binding?.realTimeWeatherShowBar?.title="${data.place.name} ${data.temperature.toInt()}°|${SkyconTransfer.transformDis(data.realTimeSkycon)}"
        binding?.realTimeSkycon?.setImageBitmap(SkyconTransfer.transformIc(data.realTimeSkycon,200*(getDpi(MainActivity.context)/160)))

    }
}