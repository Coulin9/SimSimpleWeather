package com.android.simpleweather.logic.network

import com.android.simpleweather.logic.entity.Places
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface PlaceServer {
     @GET("v2/place?token=${Token.token}&lang=zh_CN")
     fun queryPlace(@Query("query") name:String):Observable<Places>
}