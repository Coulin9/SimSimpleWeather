package com.android.simpleweather.ui.place

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.simpleweather.MainActivity
import com.android.simpleweather.R
import com.android.simpleweather.databinding.PlaceListFragLayoutBinding
import com.android.simpleweather.ui.AppViewModel
import com.android.simpleweather.utils.LogUtil
import com.android.simpleweather.utils.setBlur

class PlaceListFragment:Fragment() {
    private var binding:PlaceListFragLayoutBinding?=null

    private var adapter:PlaceAdapter?=null

    private var viewModel:AppViewModel?=null

    companion object{
        val blurRoot get() = _blurRoot!!
        private var _blurRoot:ViewGroup?=null
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel=AppViewModel.getInstance()
        binding= PlaceListFragLayoutBinding.inflate(inflater,container,false)
        _blurRoot=binding?.root
        return binding?.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        viewModel?.data?.observe(viewLifecycleOwner){
            if(it!=null){
                if(binding?.placeList?.adapter==null){
                    adapter= PlaceAdapter(it)
                    binding?.placeList?.adapter=adapter
                    binding?.placeList?.layoutManager=LinearLayoutManager(MainActivity.context)
                }else{
                    //如果只是数据改变，那么就不需要新建adapter对象
                    //怎么获取被移除的数据的下标呢？
                    //在PlaceAdapter中维护一个整数域来表示上次删除的数。
                    val tAdapter=binding?.placeList?.adapter as PlaceAdapter
                    //通知数据移除并修改adapter的数据
                    tAdapter.notifyItemRemoved(tAdapter.lastDeletePosition)
                    //不仅仅要通知adapter数据被删除了，还要通知adapter删除后的数据集下标都改变了。
                    tAdapter.notifyItemRangeRemoved(tAdapter.lastDeletePosition,1)
                    tAdapter.data=it
                }
            }
        }


        //搜索框的监听事件
        binding?.placeSearcher?.setOnQueryTextListener(object :androidx.appcompat.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextChange(newText: String?): Boolean {
                //每次搜索框文字改变都搜索地点。
                viewModel?.queryPlace(newText!!)
                if(newText==""){
                    //输入框为空，就让列表不显示
                    binding?.searchResBlur?.setBlurEnabled(false)
                    binding?.placeListMask?.visibility=View.GONE
                }else{
                    //否则显示
                    setBlur(MainActivity.context,binding?.searchResBlur!!,20f, blurRoot)
                    binding?.placeListMask?.visibility=View.VISIBLE
                }
                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }
        })
        viewModel?.places?.observe(viewLifecycleOwner){
            if(it!=null){
                binding?.placeSearchResList?.visibility=View.VISIBLE
                binding?.placeSearchResList?.adapter=PlaceSearchAdapter(it)
                binding?.placeSearchResList?.layoutManager=LinearLayoutManager(MainActivity.context)
            }
        }

        super.onActivityCreated(savedInstanceState)
    }

    override fun onPause() {
        //在该界面退出应用时应该将adapter的值置空。
        //否则会触发通知改变列表的逻辑（用来删除item的）
        binding?.placeList?.adapter=null
        super.onPause()
    }

    override fun onDestroyView() {
        binding=null
        viewModel=null
        adapter=null
        super.onDestroyView()
    }
}