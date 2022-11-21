package com.android.simpleweather.ui.weather

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.simpleweather.MainActivity
import com.android.simpleweather.R
import com.android.simpleweather.databinding.TempWeatherLayoutBinding
import com.android.simpleweather.logic.Repository
import com.android.simpleweather.logic.entity.Place
import com.android.simpleweather.logic.entity.PlaceWithWeather
import com.android.simpleweather.ui.AppViewModel
import com.android.simpleweather.utils.*
import com.google.gson.Gson

class TempWeatherFragment:Fragment() {

    private var binding:TempWeatherLayoutBinding?=null

    private var adapter1:HourlyForecastAdapter?=null

    private var adapter2:DailyForecastAdapter?=null

    private val MIN_PRESSURE=86400f

    private val MAX_PRESSURE=107900f

    private val viewModel=AppViewModel.getInstance()

    private var data:PlaceWithWeather?=null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= TempWeatherLayoutBinding.inflate(inflater,container,false)
        val place= Gson().fromJson(arguments?.get("clickedPlace") as String,Place::class.java)
        Repository.refreshPlace(place)
            .subscribe {
                if(it.status=="ok"){
                    bindData(it)
                    data=it
                }else Toast.makeText(MainActivity.context,"网络请求失败！", Toast.LENGTH_SHORT).show()
            }
        return binding?.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {

        binding?.cancel?.setOnClickListener {
            MainActivity.context.onBackPressed()
        }
        binding?.add?.setOnClickListener {
            //添加地点
            if(viewModel?.addData(data!!)!!){
                MainActivity.navControl.navigate(R.id.tempWeatherFragmentToWeatherFragment, bundleOf("position" to  viewModel.data.value?.size))
                Toast.makeText(MainActivity.context,"添加成功！",Toast.LENGTH_SHORT).show()
            }
        }
        super.onActivityCreated(savedInstanceState)
    }

    override fun onDestroyView() {
        binding=null
        super.onDestroyView()
    }

    private fun bindData(data: PlaceWithWeather){
        val blurRadius=30f

        setBlur(MainActivity.context,binding?.barBlur!!,blurRadius,binding?.blurRoot as ViewGroup)


        //空气质量
        //设置卡片的高斯模糊效果，注意parent是你想要模糊化的那个背景，这里就是WeatherFragment.blurRoot
        //setBlur(MainActivity.context,binding?.aqiBlur!!,blurRadius,binding?.blurRoot as ViewGroup)
        if(data.realTimeAirQuality!=null){
            binding?.numAndDis?.text="${data.realTimeAirQuality?.aqi?.chn} - ${data.realTimeAirQuality?.description?.chn}"
            binding?.detailedDis?.text="当前AQI(CN)为${data.realTimeAirQuality?.aqi?.chn}。"
        }
        //LogUtil.d("bindData",data.toString())
        //小时级天气预报
        //setBlur(MainActivity.context,binding?.hourlyForecastBlur!!,blurRadius,binding?.blurRoot as ViewGroup)
        if(data.hourlyForecast!=null){
            adapter1=HourlyForecastAdapter(data.hourlyForecast!!)
            binding?.hourlyForecastList?.adapter=adapter1
            binding?.hourlyForecastList?.layoutManager=
                LinearLayoutManager(MainActivity.context, LinearLayoutManager.HORIZONTAL,false)
        }
        //天级别天气预报
        //setBlur(MainActivity.context,binding?.dailyForecastBlur!!,blurRadius,binding?.blurRoot as ViewGroup)
        if(data.dailyForecast!=null){
            adapter2= DailyForecastAdapter(data.dailyForecast!!)
            binding?.dailyForecastList?.adapter=adapter2
            binding?.dailyForecastList?.layoutManager= LinearLayoutManager(MainActivity.context)
        }
        //向下短波通量
        //setBlur(MainActivity.context,binding?.dswrfBlur!!,blurRadius,binding?.blurRoot as ViewGroup)
        binding?.dswrfValue?.text="${data.dswrf.toInt()}"
        //风速风向
        //setBlur(MainActivity.context,binding?.windBlur!!,blurRadius,binding?.blurRoot as ViewGroup)
        if(data.wind!=null){
            val direction=if(data.wind?.direction!!>180F) data.wind?.direction!!-180F else data.wind?.direction!!+180F
            ObjectAnimator.ofFloat(binding?.windDirectImage,"rotation",direction!!)
                .setDuration(500)
                .start()
            binding?.windDis?.text="${directionToDisc(data.wind?.direction!!)} - ${data?.wind?.speed?.toInt()!!}千米/时"
        }
        //体感温度
        //setBlur(MainActivity.context,binding?.appTempBlur!!,blurRadius,binding?.blurRoot as ViewGroup)
        binding?.appTempValue?.text="${data.appTemp}°"
        //湿度
        //setBlur(MainActivity.context,binding?.humidityBlur!!,blurRadius,binding?.blurRoot as ViewGroup)
        binding?.humidityValue?.text="${(data.humidity*100).toInt()}%"
        binding?.humidityDis?.text="当前露点温度为${calculateDropTemp(data.temperature,data.humidity*100).toInt()}°。"
        //能见度
        //setBlur(MainActivity.context,binding?.visibilityBlur!!,20f,binding?.blurRoot as ViewGroup)
        binding?.visibilityValue?.text="${data.visibility.toInt()}公里"
        binding?.visibilityDis?.text="目前${getVisibilityDisc(data.visibility)}。"
        //气压
        //setBlur(MainActivity.context,binding?.pressureBlur!!,blurRadius,binding?.blurRoot as ViewGroup)
        if(data.pressure!=0f){
            binding?.pressureValue?.text="${(data.pressure/100).toInt().toFloat()/10}千帕"
            val movePxs= ((156-8).toFloat() *((data.pressure-MIN_PRESSURE)/(MAX_PRESSURE-MIN_PRESSURE)))*(getDpi(
                MainActivity.context) /160)
            ObjectAnimator.ofFloat(binding?.pressureArrow!!,"translationX",movePxs)
                .setDuration(500)
                .start()
        }
        //实时天气
        //setBlur(MainActivity.context,binding?.realTimeBlur!!,blurRadius,binding?.blurRoot as ViewGroup)
        binding?.realTimeWeatherShowBar?.title="${data.place.name} ${data.temperature.toInt()}°|${SkyconTransfer.transformDis(data.realTimeSkycon)}"
        binding?.realTimeSkycon?.setImageBitmap(
            SkyconTransfer.transformIc(data.realTimeSkycon,200*(getDpi(
                MainActivity.context) /160)))

    }

}