package com.android.simpleweather

import android.app.Activity
import android.app.ActivityManager
import android.app.StatusBarManager
import android.content.ContextParams
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.service.notification.StatusBarNotification
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.amap.api.location.AMapLocationClient
import com.android.simpleweather.logic.entity.*
import com.android.simpleweather.logic.network.NetWork
import com.android.simpleweather.ui.AppViewModel
import com.android.simpleweather.utils.LocationS
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.jar.Manifest
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    companion object{
        val sharedPref get() = _sharedPref!!
        private var _sharedPref:SharedPreferences?=null
        val context get() = _context!!
        private var _context:Activity?=null
        val r get() = _r!!
        private var _r:Resources?=null
        val window get() = _window
        private var _window:Window?=null
        val navControl get() = _navControl!!
        private var _navControl:NavController?=null

        val navHostFrag get() = _navHostFrag
        private var _navHostFrag:Fragment?=null

        val locationClient get() = _locationClient!!
        private var _locationClient:AMapLocationClient?=null
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        _sharedPref=getPreferences(MODE_PRIVATE)
        _context=this
        _r=resources
        _window=window
        _navControl=(supportFragmentManager.findFragmentById(R.id.navHost) as NavHostFragment).navController
        _navHostFrag=supportFragmentManager.findFragmentById(R.id.navHost)

        //让状态栏与手势导航小横条透明
        window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        _locationClient=LocationS.getInstance()
    }

    override fun onStart() {
        startLocation()
        checkPermission()
        super.onStart()
    }

    override fun onStop() {
        stopLocation()
        super.onStop()
    }

    override fun onDestroy() {
        destroyLocationClient()
        super.onDestroy()
    }

    private fun startLocation(){
        if(locationClient!=null){
            locationClient.startLocation()
        }
    }

    private fun stopLocation(){
        if(locationClient!=null){
            locationClient.stopLocation()
        }
    }

    private fun destroyLocationClient(){
        _locationClient=null
        LocationS.destroyInstance()
    }

    private fun checkPermission(){
        /*val requestPermissionLauncher=registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){res->
            var flag=true
            for(r in res){
                //检查是否授予了所有权限
                flag=flag&&r.value
            }
            if(flag){
                location()
            }else{
                Toast.makeText(this,"请授予定位权限！",Toast.LENGTH_SHORT).show()
                navControl.navigate(R.id.weatherToPlaceList)
            }
        }*/

        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED
            ||ContextCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_BACKGROUND_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION,android.Manifest.permission.ACCESS_BACKGROUND_LOCATION),0)
            }else ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),0)
        }
    }

    fun location(){
        locationClient.setLocationListener { location->
            if (location.errorCode == 0) {
                AppViewModel.getInstance()?.setLocationPlace(Place("",location.district,location.address,
                    Location(location.latitude,location.longitude)
                ))
            }else {
                //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                Log.e("AmapError","location Error, ErrCode:"
                        + location.errorCode + ", errInfo:"
                        + location.errorInfo);
            }
            //不论定位结果如何，但必须定位完成后再刷新天气。
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode==0){
            var flag=true
            for(r in grantResults){
                flag=flag&&(r==PackageManager.PERMISSION_GRANTED)
            }
            if(!flag){
                Toast.makeText(this,"请授予定位权限！",Toast.LENGTH_SHORT).show()
                navControl.navigate(R.id.weatherToPlaceList)
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


}