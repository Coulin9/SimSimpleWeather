package com.android.simpleweather.ui.weather

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.simpleweather.MainActivity
import com.android.simpleweather.databinding.HourlyForecastItemLayoutBinding
import com.android.simpleweather.logic.entity.PlaceWithWeather
import com.android.simpleweather.utils.SkyconTransfer
import com.android.simpleweather.utils.dateToHourlyDis
import com.android.simpleweather.utils.setBlur

class HourlyForecastAdapter(val data:List<PlaceWithWeather.HourlyForecastItem>):RecyclerView.Adapter<HourlyForecastAdapter.ViewHolder>() {
    inner class ViewHolder(val binding:HourlyForecastItemLayoutBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding=HourlyForecastItemLayoutBinding.inflate(LayoutInflater.from(MainActivity.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.hourlyTemp.text="${data[position].temperature.toInt()}°"
        holder.binding.hourlyTime.text= dateToHourlyDis(data[position].dateTime)
        holder.binding.skycon.setImageBitmap(SkyconTransfer.transformIc(data[position].skycon,48))
    }

    override fun getItemCount(): Int {
        return data.size
    }
}