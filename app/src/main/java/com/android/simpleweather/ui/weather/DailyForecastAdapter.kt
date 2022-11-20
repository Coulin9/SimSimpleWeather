package com.android.simpleweather.ui.weather

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.simpleweather.MainActivity
import com.android.simpleweather.databinding.DailyForecastItemLayoutBinding
import com.android.simpleweather.logic.entity.PlaceWithWeather
import com.android.simpleweather.utils.SkyconTransfer
import com.android.simpleweather.utils.dateToWeekDay

class DailyForecastAdapter(val data:List<PlaceWithWeather.DailyForecastItem>):RecyclerView.Adapter<DailyForecastAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: DailyForecastItemLayoutBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding=DailyForecastItemLayoutBinding.inflate(LayoutInflater.from(MainActivity.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.dailyTemp.text="${data[position].minTemp.toInt()}°-${data[position].maxTemp.toInt()}°"
        holder.binding.dailyTime.text= dateToWeekDay(data[position].date)
        holder.binding.skycon.setImageBitmap(SkyconTransfer.transformIc(data[position].skycon,56))
        if(position==data.size-1) holder.binding.dailyItemDivider.visibility= View.INVISIBLE
    }

    override fun getItemCount(): Int {
        return data.size
    }
}