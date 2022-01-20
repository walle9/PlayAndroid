package com.walle.playandroid.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.walle.playandroid.R
import com.walle.playandroid.databinding.ItemHomeArticleBinding
import com.walle.playandroid.databinding.ItemHomeBannerBinding
import com.walle.playandroid.databinding.ItemSearchHotKeyBinding
import com.walle.playandroid.model.Article
import com.walle.playandroid.model.Banner
import com.walle.playandroid.model.HotKey
import com.walle.playandroid.viewmodel.SearchViewModel

class HotKeyViewHolder(private val binding: ItemSearchHotKeyBinding):RecyclerView.ViewHolder(binding.root) {

    lateinit var searchViewModel: SearchViewModel
    lateinit var item: HotKey

    fun bind(item:HotKey,position:Int,searchViewModel:SearchViewModel) {
        binding.data = item
        this.item = item
        binding.pos = position+1

        this.searchViewModel = searchViewModel
    }

    init {
        binding.root.setOnClickListener {
            searchViewModel.searchEdit.value = item.name
            searchViewModel.search(item.name)
        }
    }

    companion object{
        fun create(parent:ViewGroup):HotKeyViewHolder{
            val itemSearchHotKeyBinding = DataBindingUtil.inflate<ItemSearchHotKeyBinding>(LayoutInflater.from(parent.context), R.layout.item_search_hot_key,parent,false)
            return HotKeyViewHolder(itemSearchHotKeyBinding)
        }
    }
}