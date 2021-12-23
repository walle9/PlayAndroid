package com.walle.playandroid.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.walle.playandroid.R
import com.walle.playandroid.adapter.ArticleLoadStateAdapter
import com.walle.playandroid.adapter.HomeArticleAdapter
import com.walle.playandroid.databinding.FragmentHomeBinding
import com.walle.playandroid.databinding.ItemHomeArticleBinding
import com.walle.playandroid.viewmodel.HomeViewModel
import kotlinx.coroutines.launch


class HomeFragment : Fragment() {
    private val model: HomeViewModel by activityViewModels()
    private lateinit var binding: FragmentHomeBinding
    private val adapter = HomeArticleAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_home,container,false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.retryButton.setOnClickListener {
            adapter.retry()
        }

        binding.refresh.setOnRefreshListener {
            adapter.refresh()
        }

        binding.rvHome.addItemDecoration(DividerItemDecoration(requireContext(),DividerItemDecoration.VERTICAL))
        binding.rvHome.layoutManager = LinearLayoutManager(requireContext())

        initAdapter()

        if (!model.article.hasObservers()) {
            model.article.observe(viewLifecycleOwner, {
                binding.refresh.isRefreshing=false
                lifecycleScope.launch { adapter.submitData(it) }
            })
        }
    }

    private fun initAdapter() {
        binding.rvHome.adapter = adapter.withLoadStateHeaderAndFooter(
            header = ArticleLoadStateAdapter{adapter.retry()},
            footer = ArticleLoadStateAdapter{adapter.retry()}
        )
        adapter.addLoadStateListener {
            // show empty list
            val isListEmpty = it.refresh is LoadState.NotLoading && adapter.itemCount == 0
            showEmptyList(isListEmpty)

            // Only show the list if refresh succeeds.
            binding.rvHome.isVisible = it.source.refresh is LoadState.NotLoading
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
                    requireContext(),
                    "\uD83D\uDE28 Wooops ${it.error}",
                    Toast.LENGTH_LONG
                ).show()}
        }
    }

    private fun showEmptyList(show: Boolean) {
        if (show) {
            binding.emptyList.visibility = View.VISIBLE
            binding.rvHome.visibility = View.GONE
        } else {
            binding.emptyList.visibility = View.GONE
            binding.rvHome.visibility = View.VISIBLE
        }
    }

}