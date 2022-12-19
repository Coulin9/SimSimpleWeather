package com.android.simpleweather

import android.app.Activity
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.baidu.location.LocationClient
import com.baidu.location.LocationClientOption

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
        private var _locationClient:LocationClient?=null

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

        //初始化定位服务。
        initLocation()

    }


    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }

    override fun onStart() {
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

    //初始化定位服务
    fun initLocation(){
        LocationClient.setAgreePrivacy(true)
        val option=LocationClientOption()
        option.apply {
            setLocationPurpose(LocationClientOption.BDLocationPurpose.SignIn)
            openGps=true
            setIsNeedAddress(true)
            setIsNeedLocationDescribe(true)
        }
        _locationClient=LocationClient(this.applicationContext)
        _locationClient?.locOption=option
    }
    private fun startLocation(){
        if(locationClient!=null){
            locationClient.start()
        }
    }

    private fun stopLocation(){
        if(locationClient!=null){
            locationClient.stop()
        }
    }

    private fun destroyLocationClient(){
        _locationClient=null
    }

    //申请定位权限
    private fun checkPermission(){
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),0)
        }else startLocation()
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
            }else startLocation()
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


}