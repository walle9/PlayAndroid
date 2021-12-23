package com.walle.playandroid.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.walle.playandroid.R
import com.walle.playandroid.databinding.ItemHomeArticleBinding
import com.walle.playandroid.model.Article

class ArticleViewHolder(private val binding: ItemHomeArticleBinding):RecyclerView.ViewHolder(binding.root) {

    init {
        binding.root.run {
            setOnClickListener {
                Toast.makeText(context, "点击了", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun bind(item:Article.Data?) {
        binding.data = item
    }

    companion object{
        fun creat(parent:ViewGroup):ArticleViewHolder{
            val itemHomeArticleBinding = DataBindingUtil.inflate<ItemHomeArticleBinding>(LayoutInflater.from(parent.context), R.layout.item_home_article,parent,false)
            return ArticleViewHolder(itemHomeArticleBinding)
        }
    }
}