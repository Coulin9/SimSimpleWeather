package com.android.simpleweather.utils

import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

object Ptype {
    //获取容器的实际类型。
    fun getType(raw:Class<*>,vararg args:Type)=object :ParameterizedType{
        override fun getRawType(): Type=raw
        override fun getActualTypeArguments(): Array<out Type> = args
        override fun getOwnerType(): Type?=null
    }
}