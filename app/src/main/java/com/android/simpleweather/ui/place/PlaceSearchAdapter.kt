package com.android.simpleweather.ui.place

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.RecyclerView
import com.android.simpleweather.MainActivity
import com.android.simpleweather.R
import com.android.simpleweather.databinding.PlaceSearchResItemLayoutBinding
import com.android.simpleweather.logic.entity.Place
import com.google.gson.Gson

class PlaceSearchAdapter(val data:List<Place>):RecyclerView.Adapter<PlaceSearchAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: PlaceSearchResItemLayoutBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding=PlaceSearchResItemLayoutBinding.inflate(LayoutInflater.from(MainActivity.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.placeName.text=data[position].name

        holder.binding.root.setOnClickListener {
            val bundle= bundleOf("clickedPlace" to Gson().toJson(data[position]))
            MainActivity.navControl.navigate(R.id.placeListToTempWeather,bundle)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}