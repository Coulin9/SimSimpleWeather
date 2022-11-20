package com.android.simpleweather.logic

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.android.simpleweather.MainActivity
import com.android.simpleweather.logic.entity.*
import com.android.simpleweather.logic.network.NetWork
import com.android.simpleweather.ui.AppViewModel
import com.android.simpleweather.utils.LogUtil
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableSource
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

object Repository {

    fun refresh(data:MutableLiveData<List<PlaceWithWeather>>){
        val places = data.value as MutableList<PlaceWithWeather>?
        val res=Array<PlaceWithWeather?>(places?.size!!){
            null
        }
        var eFlag=false
        for(i in places.indices){
            val lat=places[i].place.location.lat
            val lng=places[i].place.location.lng
            //将两个请求（实时天气与天气预报）结合起来
            Observable.zip(NetWork.getForecast(lat, lng),NetWork.getRealTimeWeather(lat, lng)){forecast,realtime->
                if(forecast.status=="ok"&&realtime.status=="ok"){
                    return@zip PlaceWithWeather("ok",places[i].place,realtime,forecast)
                }else return@zip PlaceWithWeather("failed",places[i].place,null,null)
            }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object :Observer<PlaceWithWeather>{
                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onNext(t: PlaceWithWeather) {
                        if(t.status=="ok") res[i]=t
                        else{
                            res[i]=places[i]
                            if(!eFlag){
                                Toast.makeText(MainActivity.context,"网络请求失败！", Toast.LENGTH_SHORT).show()
                                eFlag=true
                            }
                        }
                    }

                    override fun onError(e: Throwable) {
                        res[i]=places[i]
                    }

                    override fun onComplete() {
                        //当所有的请求都返回时，给liveData赋值。
                        var tFlag=true
                        for(r in res){
                            if(r==null){
                                tFlag=false
                                break
                            }
                        }
                        if(tFlag){
                            val tempList= mutableListOf<PlaceWithWeather>()
                            for(r in res){
                                tempList.add(r!!)
                            }
                            data.value=tempList
                            //存入本地缓存
                            MainActivity.sharedPref.edit().apply{
                                putString("WeatherCache",Gson().toJson(tempList))
                                apply()
                            }
                        }
                    }
                })
        }
    }


    fun queryPlaces(query:String,data:MutableLiveData<List<Place>>){
        NetWork.queryPlace(query)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { places->
                if(places.status=="ok"){
                    data.value=places.places
                }else{
                    data.value= listOf()
                    //Toast.makeText(MainActivity.context,"找不到地点！", Toast.LENGTH_SHORT).show()
                }
            }
    }

    fun refreshPlace(place: Place):Observable<PlaceWithWeather>{
        val lat=place.location.lat
        val lng=place.location.lng
        return Observable.zip(NetWork.getForecast(lat, lng),NetWork.getRealTimeWeather(lat, lng)){forecast,realtime->
            if(forecast.status=="ok"&&realtime.status=="ok"){
                return@zip PlaceWithWeather("ok",place,realtime,forecast)
            }else return@zip PlaceWithWeather("failed",place,null,null)
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}