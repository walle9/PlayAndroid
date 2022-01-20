package com.walle.playandroid.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.walle.playandroid.model.Banner
import com.walle.playandroid.model.HotKey
import com.walle.playandroid.viewmodel.SearchViewModel

class HotKeyAdapter(private val searchViewModel: SearchViewModel):RecyclerView.Adapter<HotKeyViewHolder>() {
    private var mData:List<HotKey> = arrayListOf()
    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: List<HotKey>) {
        mData = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HotKeyViewHolder {
        return HotKeyViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: HotKeyViewHolder, position: Int) {
        holder.bind(mData[position],position,searchViewModel)
    }

    override fun getItemCount(): Int {
       return mData.size
    }


}