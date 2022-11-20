package com.android.simpleweather.ui.place

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.RecyclerView
import com.android.simpleweather.MainActivity
import com.android.simpleweather.R
import com.android.simpleweather.databinding.PlaceItemLayoutBinding
import com.android.simpleweather.logic.entity.PlaceWithWeather
import com.android.simpleweather.ui.AppViewModel
import com.android.simpleweather.utils.*
import java.util.*

class PlaceAdapter(var data:List<PlaceWithWeather>):RecyclerView.Adapter<PlaceAdapter.ViewHolder>() {
    inner class ViewHolder(val binding:PlaceItemLayoutBinding):RecyclerView.ViewHolder(binding.root)

    val lastDeletePosition get() = _lastDeletePosition
    private var _lastDeletePosition=0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding=PlaceItemLayoutBinding.inflate(LayoutInflater.from(MainActivity.context),parent,false)
        return ViewHolder(binding)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding=holder.binding
        setBlur(MainActivity.context,binding.placeItemBlur,50f,PlaceListFragment.blurRoot)
        if(position==0){
                if(data[position].dailyForecast!=null&&data[position].hourlyForecast!=null){
                    LogUtil.d("PlaceAdapter",data[position].status)
                    binding.bigTitle.text="我的位置"
                    binding.smallTitle.text=data[position].place.name
                    binding.skyconDis.text=SkyconTransfer.transformDis(data[position].realTimeSkycon)
                    binding.curTemp.text="${data[position].temperature.toInt()}°"
                    binding.temp.text="最低${data[position].dailyForecast!![0].minTemp.toInt()}° 最高${data[position].dailyForecast!![0].maxTemp.toInt()}°"
                    binding.skyconImage.setImageBitmap(SkyconTransfer.transformIc(data[position].realTimeSkycon,200))
                }
        }else{
                if(data[position].dailyForecast!=null&&data[position].hourlyForecast!=null){
                    val date=Date()
                    binding.bigTitle.text=data[position].place.name
                    binding.smallTitle.text="${dateToHourlyDis2(date)}"
                    binding.skyconDis.text=SkyconTransfer.transformDis(data[position].realTimeSkycon)
                    binding.curTemp.text="${data[position].temperature.toInt()}°"
                    binding.temp.text="最低${data[position].dailyForecast!![0].minTemp.toInt()}° 最高${data[position].dailyForecast!![0].maxTemp.toInt()}°"
                    binding.skyconImage.setImageBitmap(SkyconTransfer.transformIc(data[position].realTimeSkycon,200))
                }
        }
        binding.contentView.setOnClickListener {
            MainActivity.navControl.navigate(R.id.placeListToWeather, bundleOf("position" to position))
        }
        //检测横向的滑动
        var startX=0f
        var lastX=0f
        var anime1:ValueAnimator?=null
        var anime2:ValueAnimator?=null
        binding.contentView.setOnTouchListener { v, event ->
            if(position==0){
                //第一个是定位得到的地点，不允许删除
                if(event.action==MotionEvent.ACTION_UP) v.performClick()
                return@setOnTouchListener true
            }
            val dX=event.rawX-lastX
            val tDX=event.rawX-startX
            val dL=binding.deletePlaceLayout
            fun performAnime(){
                if(v.right>(v.width/4)*3&&v.right<v.width){
                    if(tDX<0){
                        if(anime1==null){
                            anime1=ValueAnimator.ofInt((v.left+dX).toInt(),0-v.width/4).apply {
                                duration=durationS.toLong()
                                addUpdateListener {
                                    val left=it.animatedValue as Int
                                    v.layout(left,v.top,left+v.width,v.bottom)
                                    dL.layout(left+v.width,dL.top,left+v.width+dL.width,dL.bottom)
                                }
                            }
                        }
                        anime1?.start()
                    }else if(tDX>0){
                        if(anime2==null){
                            anime2=ValueAnimator.ofInt((v.left+dX).toInt(),0).apply {
                                duration=durationS.toLong()
                                addUpdateListener {
                                    val left=it.animatedValue as Int
                                    v.layout(left,v.top,left+v.width,v.bottom)
                                    dL.layout(left+v.width,dL.top,left+v.width+dL.width,dL.bottom)
                                }
                            }
                        }
                        anime2?.start()
                    }
                }
            }


            //LogUtil.d("PlaceTouch","${v.right},${dL.right},${v.left},${dL.left},${v.x},${v.translationX},${v.scrollX}.${event.rawX-startX}")
            if(event.action==MotionEvent.ACTION_DOWN){
                //这里不要使用x要使用rawX，因为x的参考系是v本身，重新布局后会造成坐标值错误。
                lastX=event.rawX
                startX=event.rawX
            }else if(event.action==MotionEvent.ACTION_MOVE){
                //手势可以打断动画
                if(anime1!=null&&anime1?.isRunning!!) anime1?.pause()
                if(anime2!=null&&anime2?.isRunning!!) anime2?.pause()
                //移动后的位置如果不在四分之一范围内，那么就不会进行布局
                if((v.left+dX>(0-v.width/4)||dX>0)&&(v.left+dX<0||dX<0)) {
                    //两个视图同时移动
                    v.layout(v.left+dX.toInt(),v.top,v.right+dX.toInt(),v.bottom)
                    dL.layout(dL.left+dX.toInt(),dL.top,dL.right+dX.toInt(),dL.bottom)
                }
                lastX = event.rawX
            }else if(event.action==MotionEvent.ACTION_UP){
                //LogUtil.d("PlaceTouch","UP")
                if(tDX==0f&&v.right==v.width) v.performClick()
                performAnime()
            }else if(event.action==MotionEvent.ACTION_CANCEL||event.action==MotionEvent.ACTION_MASK){
                //LogUtil.d("PlaceTouch","${event.action}")
                performAnime()
            }

            true
        }

        binding.deletePlaceBtn.setOnClickListener {
            Toast.makeText(MainActivity.context,"长按以删除",Toast.LENGTH_SHORT).show()
        }
        binding.deletePlaceBtn.setOnLongClickListener {
            _lastDeletePosition=position
            val vm=AppViewModel.getInstance()
            if(vm?.deletePlace(position)!!){
                Toast.makeText(MainActivity.context,"删除成功",Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(MainActivity.context,"出现错误",Toast.LENGTH_SHORT).show()
            }
            true
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}