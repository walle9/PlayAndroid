package com.walle.playandroid.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.walle.playandroid.R
import com.walle.playandroid.databinding.LoadStateFooterViewItemBinding

class ArticleLoadStateViewHolder(private val binding: LoadStateFooterViewItemBinding, retry: () -> Unit) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.retryButton.setOnClickListener {
            retry()
        }
    }

    fun bind(loadstate: LoadState) {
        binding.data = loadstate
        if (loadstate is LoadState.Error) {
            binding.errorMsg.text = loadstate.error.localizedMessage
        }

    }
    companion object {
        fun create(parent:ViewGroup,retry: () -> Unit):ArticleLoadStateViewHolder{
            val view=LayoutInflater.from(parent.context).inflate(R.layout.load_state_footer_view_item,parent,false)
            val binding = LoadStateFooterViewItemBinding.bind(view)
            return ArticleLoadStateViewHolder(binding, retry)
        }
    }
}