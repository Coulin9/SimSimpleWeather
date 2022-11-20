package com.android.simpleweather.utils

import android.annotation.SuppressLint
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.android.simpleweather.MainActivity

object LocationS{
    @SuppressLint("StaticFieldLeak")
    private var mLocationClient:AMapLocationClient?=null

    private var option:AMapLocationClientOption?=null
    fun getInstance():AMapLocationClient?{
        if(mLocationClient==null){
            //隐私合规检查
            AMapLocationClient.updatePrivacyShow(MainActivity.context.applicationContext,true,true)
            AMapLocationClient.updatePrivacyAgree(MainActivity.context.applicationContext,true)
            try {
                mLocationClient= AMapLocationClient(MainActivity.context.applicationContext)
                option=AMapLocationClientOption()
                //设置定位目的
                option?.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.SignIn)
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
        mLocationClient?.setLocationOption(option)
        return mLocationClient
    }

    fun destroyInstance(){
        if(mLocationClient!=null){
            mLocationClient=null
        }
    }
}