package com.walle.playandroid.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.walle.playandroid.R
import com.walle.playandroid.databinding.ItemHomeArticleBinding
import com.walle.playandroid.model.Article
import com.walle.playandroid.ui.activity.WebActivity

class ArticleViewHolder(private val binding: ItemHomeArticleBinding) : RecyclerView.ViewHolder(binding.root) {
    private var data: Article.Data?=null
    init {
        binding.root.run {
            setOnClickListener {
                WebActivity.actionStart(context,data?.link)
            }
        }
    }

    fun bind(item: Article.Data?) {
        binding.data = item
        data= item
    }

    companion object {
        fun creat(parent: ViewGroup): ArticleViewHolder {
            val itemHomeArticleBinding = DataBindingUtil.inflate<ItemHomeArticleBinding>(
                LayoutInflater.from(parent.context),
                R.layout.item_home_article,
                parent,
                false
            )
            return ArticleViewHolder(itemHomeArticleBinding)
        }
    }
}