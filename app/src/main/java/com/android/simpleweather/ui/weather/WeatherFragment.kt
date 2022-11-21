package com.android.simpleweather.ui.weather

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.viewpager2.widget.ViewPager2
import com.android.simpleweather.MainActivity
import com.android.simpleweather.R
import com.android.simpleweather.databinding.WeatherFragLayoutBinding
import com.android.simpleweather.logic.entity.PlaceWithWeather
import com.android.simpleweather.logic.network.NetWork
import com.android.simpleweather.ui.AppViewModel
import com.android.simpleweather.utils.LogUtil
import com.android.simpleweather.utils.setBlur
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class WeatherFragment:Fragment() {
    private var  binding:WeatherFragLayoutBinding?=null

    private var viewModel:AppViewModel?=null


    companion object{
        val blurRoot get() = _blurRoot!!
        private var _blurRoot:ViewGroup?=null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //LogUtil.d("WF:onCreateView",this.toString())
        binding=WeatherFragLayoutBinding.inflate(inflater,container,false)
        viewModel=AppViewModel.getInstance()
        _blurRoot=binding?.root
        return binding?.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {

        //监听LiveData并更新UI
        var firstShow=true
        //每次注册一个新的Observer都会触发该Observer，以下代码每次都注册了一个新的Observer
        viewModel?.data?.observe(viewLifecycleOwner){data->
            if(firstShow){
                viewModel?.refresh()
                firstShow=false
            }
            binding?.viewPager?.adapter=WeatherViewPagerAdapter(data,requireActivity())
            //初始化DotIndicator
            binding?.indicator?.setDotNum(viewModel?.data?.value?.size!!)
            val position=arguments?.getInt("position",0)
            if(position!=null) binding?.viewPager?.setCurrentItem(position,false)
        }

        //监听并更新indicator
        binding?.viewPager?.registerOnPageChangeCallback(object :ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                binding?.indicator?.setSelectedDot(position)
            }
        })



        //为列表按钮设置点击监听
        binding?.listBtn?.setOnClickListener {
            MainActivity.navControl.navigate(R.id.weatherToPlaceList)
        }

        //设置背景模糊
        setBlur(MainActivity.context,binding?.indicatorBlur!!,20f,binding?.root!! as ViewGroup)
        //setBlur(MainActivity.context,binding?.bcBlur!!,10f,binding?.root!! as ViewGroup)
        super.onActivityCreated(savedInstanceState)
    }

    override fun onStart() {
        //LogUtil.d("WF:onStart",this.toString())
        super.onStart()
    }

    override fun onResume() {
        //LogUtil.d("WF:onResume",this.toString())
        super.onResume()
    }


    override fun onDestroyView() {
        //LogUtil.d("WF:onDestroyView",this.toString())
        binding=null
        viewModel=null
        super.onDestroyView()
    }
}