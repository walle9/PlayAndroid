package com.walle.playandroid.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.walle.playandroid.R
import com.walle.playandroid.databinding.ItemHomeArticleBinding
import com.walle.playandroid.databinding.ItemHomeBannerBinding
import com.walle.playandroid.model.Article
import com.walle.playandroid.model.Banner
import com.walle.playandroid.model.HotKey
import com.walle.playandroid.ui.activity.WebActivity
import com.walle.playandroid.viewmodel.HomeViewModel
import com.walle.playandroid.viewmodel.SearchViewModel

class BannerViewHolder(private val binding: ItemHomeBannerBinding):RecyclerView.ViewHolder(binding.root) {
    lateinit var item: Banner
    init {
        binding.root.run {
            setOnClickListener {
                WebActivity.actionStart(context, item.url)
            }
        }
    }

    fun bind(item:Banner) {
        binding.data = item
        this.item = item
    }


    companion object{
        fun create(parent:ViewGroup):BannerViewHolder{
            val itemHomeBannerBinding = DataBindingUtil.inflate<ItemHomeBannerBinding>(LayoutInflater.from(parent.context), R.layout.item_home_banner,parent,false)
            return BannerViewHolder(itemHomeBannerBinding)
        }
    }
}