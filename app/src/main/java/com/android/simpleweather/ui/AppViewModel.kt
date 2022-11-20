package com.android.simpleweather.ui

import android.renderscript.RenderScript
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.simpleweather.MainActivity
import com.android.simpleweather.logic.Repository
import com.android.simpleweather.logic.entity.Location
import com.android.simpleweather.logic.entity.Place
import com.android.simpleweather.logic.entity.PlaceWithWeather
import com.android.simpleweather.utils.LogUtil
import com.android.simpleweather.utils.Ptype
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.google.gson.Gson
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlin.concurrent.thread

class AppViewModel:ViewModel() {
    companion object{
        private var instance:AppViewModel?=null
        fun getInstance():AppViewModel?{
            if(instance==null){
                instance=ViewModelProvider(MainActivity.navHostFrag!!).get(AppViewModel::class.java)
            }
            return instance
        }
    }
    val data: LiveData<List<PlaceWithWeather>> get() = _data
    private val _data=MutableLiveData<List<PlaceWithWeather>>()

    val places:LiveData<List<Place>> get() = _places
    private val _places=MutableLiveData<List<Place>>()

    init {
        Observable.create<List<PlaceWithWeather>> {
            val s = MainActivity.sharedPref.getString("WeatherCache", "")
            //集合类型的type需要使用拼装的方式手动获取
            val type = Ptype.getType(List::class.java, PlaceWithWeather::class.java)
            val cachedData = Gson().fromJson<List<PlaceWithWeather>>(s, type)
            it.onNext(cachedData)
            it.onComplete()
        }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object :Observer<List<PlaceWithWeather>>{
                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(t: List<PlaceWithWeather>) {
                    for(pww in t) LogUtil.d("SharedPref",t.toString())
                    _data.value=t
                }

                override fun onError(e: Throwable) {
                    //如果没有本地缓存，务必从网络获取天气信息。
                    refresh()
                    LogUtil.e("SharedPref","ReadError!")
                }

                override fun onComplete() {
                }
            })
    }


    //刷新所有天气数据
    fun refresh(){
        MainActivity.locationClient.registerLocationListener(object :BDAbstractLocationListener(){
            override fun onReceiveLocation(p0: BDLocation?) {
                if(p0!=null){
                    //if(p0.hasAltitude())
                    LogUtil.d("BDLocation","${p0.district}")
                    val place=Place("",p0.district,p0.addrStr, Location(p0.latitude,p0.longitude))
                    setLocationPlace(place)
                }
                Repository.refresh(_data)
            }
        })
    }

    //删除指定下标的天气
    fun deletePlace(position:Int):Boolean{
        if(position>=0&&position<data.value?.size!!){
            //不仅将ViewModel中的数据更新，同时也同步更新本地存储的数据
            var temp= mutableListOf<PlaceWithWeather>()
            temp.addAll(data.value!!)
            for(i in data.value?.indices!!){
                if(i>position) temp[i-1]=temp[i]
            }
            temp=temp.dropLast(1) as MutableList<PlaceWithWeather>
            _data.value=temp
            //异步更新，而且允许没更新上，只要再次点进天气信息界面请求天气信息就会更新本地缓存，因此不一定需要在这里更新。
            thread {
                MainActivity.sharedPref.edit().apply {
                    putString("WeatherCache",Gson().toJson(temp))
                    apply()
                }
            }
            return true
        }
        else return false
    }

    fun  queryPlace(query:String){
        Repository.queryPlaces(query,_places)
    }

    fun addData(pww:PlaceWithWeather):Boolean{
        try {
            val tempL= mutableListOf<PlaceWithWeather>()
            if(_data.value!=null) tempL.addAll(_data.value!!)
            tempL.add(pww)
            _data.value=tempL
            return true
        }catch (e:java.lang.Exception){
            e.printStackTrace()
            return false
        }
    }

    fun setLocationPlace(place: Place?):Boolean{
        if(place==null) return false
        else{
            if(_data.value==null||_data.value?.size==0||place.name!=_data.value!![0].place.name){
                val pww=PlaceWithWeather("ok",place,null,null)
                val tempList= mutableListOf<PlaceWithWeather>()
                if(_data.value!=null&&_data.value?.size!=0){
                    tempList.addAll(_data.value!!)
                    tempList[0]=pww
                }else tempList.add(pww)
                _data.value=tempList
            }
            return true
        }
    }

    //测试用方法
    fun setData(value:List<PlaceWithWeather>){
        _data.value=value
    }
}