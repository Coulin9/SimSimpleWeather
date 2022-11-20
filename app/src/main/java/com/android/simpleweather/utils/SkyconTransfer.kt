package com.android.simpleweather.utils

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import com.android.simpleweather.MainActivity
import com.android.simpleweather.R

object SkyconTransfer {
    private val map=HashMap<String,Bitmap>()

    fun transformIc(skycon:String,size:Int):Bitmap?{
        var res:Bitmap?=null
        when(skycon){
            "CLEAR_DAY"->{
                val name="CLEAR_DAY_${size}"
                if(map.get(name)==null){
                    res=ResourcesCompat.getDrawable(MainActivity.r,R.drawable.tianqi_qing,null)?.toBitmap(size,size)
                    map[name]=res!!
                }else res= map[name]
            }
            "CLEAR_NIGHT"->{
                val name="CLEAR_NIGHT_${size}"
                if(map.get(name)==null){
                    res=ResourcesCompat.getDrawable(MainActivity.r,R.drawable.tianqi_ye,null)?.toBitmap(size,size)
                    map[name]=res!!
                }else res= map[name]
            }
            "PARTLY_CLOUDY_DAY"->{
                val name="PARTLY_CLOUDY_DAY_${size}"
                if(map.get(name)==null){
                    res=ResourcesCompat.getDrawable(MainActivity.r,R.drawable.tianqi_duoyun,null)?.toBitmap(size,size)
                    map[name]=res!!
                }else res= map[name]
            }
            "PARTLY_CLOUDY_NIGHT"->{
                val name="CLOUDY_${size}"
                if(map.get(name)==null){
                    res=ResourcesCompat.getDrawable(MainActivity.r,R.drawable.tianqi_yintian,null)?.toBitmap(size,size)
                    map[name]=res!!
                }else res= map[name]
            }
            "CLOUDY"->{
                val name="CLOUDY_${size}"
                if(map.get(name)==null){
                    res=ResourcesCompat.getDrawable(MainActivity.r,R.drawable.tianqi_yintian,null)?.toBitmap(size,size)
                    map[name]=res!!
                }else res= map[name]
            }
            "LIGHT_HAZE"->{
                val name="HAZE_${size}"
                if(map.get(name)==null){
                    res=ResourcesCompat.getDrawable(MainActivity.r,R.drawable.tianqi_wu,null)?.toBitmap(size,size)
                    map[name]=res!!
                }else res= map[name]
            }
            "MODERATE_HAZE"->{
                val name="HAZE_${size}"
                if(map.get(name)==null){
                    res=ResourcesCompat.getDrawable(MainActivity.r,R.drawable.tianqi_wu,null)?.toBitmap(size,size)
                    map[name]=res!!
                }else res= map[name]
            }
            "HEAVY_HAZE"->{
                val name="HAZE_${size}"
                if(map.get(name)==null){
                    res=ResourcesCompat.getDrawable(MainActivity.r,R.drawable.tianqi_wu,null)?.toBitmap(size,size)
                    map[name]=res!!
                }else res= map[name]
            }
            "LIGHT_RAIN"->{
                val name="LIGHT_RAIN_${size}"
                if(map.get(name)==null){
                    res=ResourcesCompat.getDrawable(MainActivity.r,R.drawable.tianqi_xiaoyu,null)?.toBitmap(size,size)
                    map[name]=res!!
                }else res= map[name]
            }
            "MODERATE_RAIN"->{
                val name="MODERATE_RAIN_${size}"
                if(map.get(name)==null){
                    res=ResourcesCompat.getDrawable(MainActivity.r,R.drawable.tianqi_zhongyu,null)?.toBitmap(size,size)
                    map[name]=res!!
                }else res= map[name]
            }
            "HEAVY_RAIN"->{
                val name="HEAVY_RAIN_${size}"
                if(map.get(name)==null){
                    res=ResourcesCompat.getDrawable(MainActivity.r,R.drawable.tianqi_dayu,null)?.toBitmap(size,size)
                    map[name]=res!!
                }else res= map[name]
            }
            "STORM_RAIN"->{
                val name="STORM_RAIN_${size}"
                if(map.get(name)==null){
                    res=ResourcesCompat.getDrawable(MainActivity.r,R.drawable.tianqi_baoyu,null)?.toBitmap(size,size)
                    map[name]=res!!
                }else res= map[name]
            }
            "FOG"->{
                val name="HAZE_${size}"
                if(map.get(name)==null){
                    res=ResourcesCompat.getDrawable(MainActivity.r,R.drawable.tianqi_wu,null)?.toBitmap(size,size)
                    map[name]=res!!
                }else res= map[name]
            }
            "LIGHT_SNOW"->{
                val name="LIGHT_SNOW_${size}"
                if(map.get(name)==null){
                    res=ResourcesCompat.getDrawable(MainActivity.r,R.drawable.tianqi_xiaoxue,null)?.toBitmap(size,size)
                    map[name]=res!!
                }else res= map[name]
            }
            "MODERATE_SNOW"->{
                val name="MODERATE_SNOW_${size}"
                if(map.get(name)==null){
                    res=ResourcesCompat.getDrawable(MainActivity.r,R.drawable.tianqi_zhongxue,null)?.toBitmap(size,size)
                    map[name]=res!!
                }else res= map[name]
            }
            "HEAVY_SNOW"->{
                val name="HEAVY_SNOW_${size}"
                if(map.get(name)==null){
                    res=ResourcesCompat.getDrawable(MainActivity.r,R.drawable.tianqi_daxue,null)?.toBitmap(size,size)
                    map[name]=res!!
                }else res= map[name]
            }
            "STORM_SNOW"->{
                val name="STORM_SNOW_${size}"
                if(map.get(name)==null){
                    res=ResourcesCompat.getDrawable(MainActivity.r,R.drawable.tianqi_baoxue,null)?.toBitmap(size,size)
                    map[name]=res!!
                }else res= map[name]
            }
            "DUST"->{
                val name="DUST_${size}"
                if(map.get(name)==null){
                    res=ResourcesCompat.getDrawable(MainActivity.r,R.drawable.tianqi_shachen,null)?.toBitmap(size,size)
                    map[name]=res!!
                }else res= map[name]
            }
            "SAND"->{
                val name="DUST_${size}"
                if(map.get(name)==null){
                    res=ResourcesCompat.getDrawable(MainActivity.r,R.drawable.tianqi_shachen,null)?.toBitmap(size,size)
                    map[name]=res!!
                }else res= map[name]
            }
            else->{
                val name="CLEAR_DAY_${size}"
                if(map.get(name)==null){
                    res=ResourcesCompat.getDrawable(MainActivity.r,R.drawable.tianqi_qing,null)?.toBitmap(size,size)
                    map[name]=res!!
                }else res= map[name]
            }
        }
        return res
    }

    fun transformDis(skycon: String):String{
        when(skycon){
            "CLEAR_DAY"->return "晴"
            "CLEAR_NIGHT"->return "晴"
            "PARTLY_CLOUDY_DAY"->return "多云"
            "PARTLY_CLOUDY_NIGHT"->return "多云"
            "CLOUDY"->return "阴"
            "LIGHT_HAZE"->return "轻度雾霾"
            "MODERATE_HAZE"->return "中度雾霾"
            "HEAVY_HAZE"->return "重度雾霾"
            "LIGHT_RAIN"->return "小雨"
            "MODERATE_RAIN"->return "中雨"
            "HEAVY_RAIN"->return "大雨"
            "STORM_RAIN"->return "暴雨"
            "FOG"->return "雾"
            "LIGHT_SNOW"->return "小雪"
            "MODERATE_SNOW"->return "中雪"
            "HEAVY_SNOW"->return "大雪"
            "STORM_SNOW"->return "暴雪"
            "DUST"->return "扬尘"
            "SAND"->return "浮沙"
            else->return "晴"
        }
    }
}