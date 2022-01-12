package com.walle.playandroid.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.walle.playandroid.model.Banner

class BannerAdapter:RecyclerView.Adapter<BannerViewHolder>() {
    private var mData:List<Banner> = arrayListOf()
    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: List<Banner>) {
        mData = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        return BannerViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
        holder.bind(mData[position])
    }

    override fun getItemCount(): Int {
       return mData.size
    }


}