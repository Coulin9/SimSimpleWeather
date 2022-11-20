package com.android.simpleweather.utils

import android.app.Activity
import android.content.Context
import android.os.Build
import android.util.DisplayMetrics
import android.view.ViewGroup
import com.android.simpleweather.MainActivity
import eightbitlab.com.blurview.BlurView
import eightbitlab.com.blurview.RenderEffectBlur
import eightbitlab.com.blurview.RenderScriptBlur
import java.lang.StringBuilder
import java.util.*

fun dateToHourlyDis(date: Date):String{
    val now=Date()
    val nowHour=now.hours
    val hour=date.hours
    val sb=StringBuilder()
    if(hour==nowHour) sb.append("现在")
    else if(hour>12) sb.append("下午${hour-12}时")
    else if(hour==12) sb.append("中午")
    else sb.append("上午${hour}时")
    return sb.toString()
}

fun dateToHourlyDis2(date: Date):String{
    val hour=date.hours
    val minutes=date.minutes
    val sb=StringBuilder()
    if(hour>12) sb.append("下午${hour-12}:${if(minutes<10) "0${minutes}" else minutes}")
    else if(hour==12) sb.append("中午")
    else sb.append("上午${hour}时")
    return sb.toString()
}

fun dateToWeekDay(date:Date):String{
    val nowWeekDay=Date().day
    val weekDay=date.day
    val sb=StringBuilder()
    if(weekDay==nowWeekDay) sb.append("今天")
    else when(weekDay){
        0->sb.append("周日")
        1->sb.append("周一")
        2->sb.append("周二")
        3->sb.append("周三")
        4->sb.append("周四")
        5->sb.append("周五")
        6->sb.append("周六")
    }
    return sb.toString()
}

fun directionToDisc(direction:Float):String{
    if((direction>=0F&&direction<11.25)||(direction<360F&&direction>=348.75)) return "北凤"
    else if(direction>=11.25&&direction<33.75) return "东北北风"
    else if(direction>=33.75&&direction<56.25) return "东北风"
    else if(direction>=56.25&&direction<78.75) return "东北东风"
    else if(direction>=78.75&&direction<101.25) return "东风"
    else if(direction>=101.25&&direction<123.75) return "东南东风"
    else if(direction>=123.75&&direction<146.25) return "东南风"
    else if(direction>=146.25&&direction<168.75) return "东南南风"
    else if(direction>=168.75&&direction<191.25) return "南风"
    else if(direction>=191.25&&direction<213.75) return "西南南风"
    else if(direction>=213.75&&direction<236.25) return "西南风"
    else if(direction>=236.25&&direction<258.75) return "西南西风"
    else if(direction>=258.75&&direction<281.25) return "西风"
    else if(direction>=281.25&&direction<303.75) return "西北西风"
    else if(direction>=303.75&&direction<326.25) return "西北风"
    else if(direction>=326.25&&direction<348.75) return "西北北风"
    else return ""
}

fun calculateDropTemp(t:Float,rh:Float):Float{
    //t代表当前温度，rh表示百分比表示的相对湿度
    val a=17.27
    val b=237.7
    val y=(a*t)/(b+t)+Math.log(rh/100.0)
    return ((b*y)/(a-y)).toFloat()
}

fun getVisibilityDisc(visibility:Float):String{
    if(visibility>10) return "非常好"
    else if(visibility>8&&visibility<=10) return "好"
    else if(visibility>6&&visibility<=8) return "一般"
    else if(visibility>4&&visibility<=6) return "较差"
    else if(visibility>2&&visibility<=4) return "差"
    else if(visibility>0&&visibility<=2) return "极差"
    else return "非常差"
}

fun getDpi(activity: Activity):Int{
    val dm=DisplayMetrics()
    activity.windowManager.defaultDisplay.getMetrics(dm)
    return dm.densityDpi
}

fun setBlur(context: Context,blurView: BlurView,radius:Float,parent: ViewGroup){
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        blurView.setupWith(parent, RenderEffectBlur())
            ?.setBlurRadius(radius)
            ?.setBlurEnabled(true)
    }else{
        blurView.setupWith(parent, RenderScriptBlur(context))
            ?.setBlurRadius(radius)
            ?.setBlurEnabled(true)
    }
}

val durationS=MainActivity.context.resources.getInteger(android.R.integer.config_shortAnimTime)