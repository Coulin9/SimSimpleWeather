package com.android.simpleweather.ui.weather

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.android.simpleweather.logic.entity.PlaceWithWeather

class WeatherViewPagerAdapter(val placeWithWeathers:List<PlaceWithWeather>,activity: FragmentActivity):FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return placeWithWeathers.size
    }

    override fun createFragment(position: Int): Fragment {
        return WeatherInfoFragment(placeWithWeathers[position])
    }
}