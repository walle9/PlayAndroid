package com.walle.playandroid.ui.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.walle.playandroid.databinding.ActivitySearchBinding
import android.graphics.Color
import android.view.View


import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.walle.playandroid.R
import com.walle.playandroid.adapter.*
import com.walle.playandroid.viewmodel.SearchViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch


class SearchActivity : AppCompatActivity() {

    private val model: SearchViewModel by viewModels()
    private lateinit var binding: ActivitySearchBinding
    private val searchArticleAdapter = HomeArticleAdapter()
    private val hotKeyAdapter by lazy {
        HotKeyAdapter(model)
    }
    private val searchHistoryAdapter by lazy {
        SearchHistoryAdapter(model)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search)
        binding.data = model
        binding.lifecycleOwner = this
        init()
    }

    override fun onResume() {
        super.onResume()
        binding.searchView.clearFocus()
    }

    fun init() {
        binding.toolbar.setNavigationOnClickListener { finish() }
        initSearchView()
        initHotKeyRecyclerView()
        initSearchHistory()
        initSearchRecyclerView()

        model.searchHistoryList.observe(this) { history ->
            searchHistoryAdapter.setData(history.map { it.keyWord })
        }

    }

    private fun initSearchHistory() {
        binding.recyclerViewHistory.apply {
            layoutManager = LinearLayoutManager(this@SearchActivity)
            adapter = searchHistoryAdapter
        }
    }

    private fun initSearchView() {
        binding.searchView.apply {
            //onActionViewExpanded()
            setIconifiedByDefault(false)

            val searchEditText: EditText = this.findViewById(androidx.appcompat.R.id.search_src_text)
            searchEditText.setTextColor(Color.WHITE)
            searchEditText.setHintTextColor(Color.WHITE)

        }
    }

    private fun initHotKeyRecyclerView() {
        binding.recyclerViewHot.apply {
            layoutManager = GridLayoutManager(this@SearchActivity, 2)
            adapter = hotKeyAdapter
        }

        model.hotKey.observe(this) {
            hotKeyAdapter.setData(it)
        }
    }

    companion object {
        fun actionStart(context: Context) {

            val intent = Intent(context, SearchActivity::class.java)
            context.startActivity(intent)
        }
    }

    private fun initSearchRecyclerView() {

        lifecycleScope.launch {
            searchArticleAdapter.loadStateFlow
                // Only emit when REFRESH LoadState for RemoteMediator changes.
                .distinctUntilChangedBy { it.refresh }
                // Only react to cases where Remote REFRESH completes i.e., NotLoading.
                .filter { it.refresh is LoadState.NotLoading }
                .collect { binding.recyclerviewSearch.scrollToPosition(0) }
        }


        binding.recyclerviewSearch.apply {
            addItemDecoration(DividerItemDecoration(this@SearchActivity, DividerItemDecoration.VERTICAL))
            layoutManager = LinearLayoutManager(this@SearchActivity)
            adapter =
                searchArticleAdapter.withLoadStateFooter(footer = ArticleLoadStateAdapter { searchArticleAdapter.retry() })

            searchArticleAdapter.addLoadStateListener { it ->
                // show empty list
                val isListEmpty = it.refresh is LoadState.NotLoading && searchArticleAdapter.itemCount == 0
                showEmptyList(isListEmpty)

                // Only show the list if refresh succeeds.
                binding.recyclerviewSearch.isVisible = it.source.refresh is LoadState.NotLoading
                // Show loading spinner during initial load or refresh.
                binding.progressBar.isVisible = it.source.refresh is LoadState.Loading

                // Show the retry state if initial load or refresh fails.
                binding.retryButton.isVisible = it.source.refresh is LoadState.Error

                // Toast on any error, regardless of whether it came from RemoteMediator or PagingSource
                val errorState = it.source.append as? LoadState.Error
                    ?: it.source.prepend as? LoadState.Error
                    ?: it.append as? LoadState.Error
                    ?: it.prepend as? LoadState.Error
                    ?: it.source.refresh as? LoadState.Error

                errorState?.let {
                    Toast.makeText(
                        this@SearchActivity,
                        "\uD83D\uDE28 Wooops ${it.error}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            model.article.observe(this@SearchActivity, {
                //binding.refresh.isRefreshing = false
                lifecycleScope.launch { searchArticleAdapter.submitData(it) }
            })


        }


    }

    private fun showEmptyList(show: Boolean) {
        if (show) {
            binding.emptyList.visibility = View.VISIBLE
            binding.recyclerviewSearch.visibility = View.GONE
        } else {
            binding.emptyList.visibility = View.GONE
            binding.recyclerviewSearch.visibility = View.VISIBLE
        }
    }
}