package com.walle.playandroid.adapter

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.walle.playandroid.viewmodel.SearchViewModel

class SearchHistoryAdapter(private val searchViewModel: SearchViewModel):RecyclerView.Adapter<SearchHistoryViewHolder>() {
    private var mData:List<String> = arrayListOf()
    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: List<String>) {
        mData = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchHistoryViewHolder {
        return SearchHistoryViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: SearchHistoryViewHolder, position: Int) {
        holder.bind(mData[position],searchViewModel)
    }

    override fun getItemCount(): Int {
       return mData.size
    }


}