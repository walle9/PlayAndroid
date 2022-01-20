package com.walle.playandroid.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.walle.playandroid.R
import com.walle.playandroid.data.SearchHistory
import com.walle.playandroid.databinding.ItemSearchHistroyBinding
import com.walle.playandroid.viewmodel.SearchViewModel

class SearchHistoryViewHolder(private val binding: ItemSearchHistroyBinding) : RecyclerView.ViewHolder(binding.root) {
    lateinit var searchViewModel: SearchViewModel
    lateinit var item: String

    init {
        binding.apply {
            ivClose.setOnClickListener {
                searchViewModel.delete(SearchHistory(item))
            }
            root.setOnClickListener {
                searchViewModel.searchEdit.value = item
                searchViewModel.search(item)
            }
        }
    }

    fun bind(item: String, searchViewModel: SearchViewModel) {
        binding.data = item
        this.item = item
        this.searchViewModel = searchViewModel
    }

    companion object {
        fun create(parent: ViewGroup): SearchHistoryViewHolder {
            val itemSearchHotKeyBinding = DataBindingUtil.inflate<ItemSearchHistroyBinding>(
                LayoutInflater.from(parent.context),
                R.layout.item_search_histroy,
                parent,
                false
            )
            return SearchHistoryViewHolder(itemSearchHotKeyBinding)
        }
    }
}