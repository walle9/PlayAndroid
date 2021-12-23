package com.walle.playandroid.adapter

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.walle.playandroid.model.Article

class HomeArticleAdapter:PagingDataAdapter<Article.Data,ArticleViewHolder>(diffCallback = COMPARATOR) {

    companion object COMPARATOR:DiffUtil.ItemCallback<Article.Data>() {
        override fun areItemsTheSame(oldItem: Article.Data, newItem: Article.Data): Boolean {
            // Id is unique.
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Article.Data, newItem: Article.Data): Boolean {
            return oldItem == newItem
        }

    }

    override fun onBindViewHolder(viewHolder: ArticleViewHolder, position: Int) {
        val item = getItem(position)
        viewHolder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder.creat(parent)
    }
}