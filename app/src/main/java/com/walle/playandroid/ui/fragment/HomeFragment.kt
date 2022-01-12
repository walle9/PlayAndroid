package com.walle.playandroid.ui.fragment

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.walle.playandroid.R
import com.walle.playandroid.adapter.ArticleLoadStateAdapter
import com.walle.playandroid.adapter.BannerAdapter
import com.walle.playandroid.adapter.HomeArticleAdapter
import com.walle.playandroid.databinding.FragmentHomeBinding
import com.walle.playandroid.common.lifecycle.LifecycleEventDispatcher
import com.walle.playandroid.viewmodel.HomeViewModel
import kotlinx.coroutines.launch


class HomeFragment : Fragment() {
    private val model: HomeViewModel by activityViewModels()
    private lateinit var binding: FragmentHomeBinding
    private val homeArticleAdapter = HomeArticleAdapter()
    private val bannerAdapter = BannerAdapter()
    var bannerCurrentPosition = 0

    private val menuItem: MenuItem by lazy {
        binding.toolbar.menu.findItem(R.id.app_bar_search)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        binding.data = model
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.retryButton.setOnClickListener {
            homeArticleAdapter.retry()
            model.getBanners()
        }

        binding.refresh.setOnRefreshListener {
            homeArticleAdapter.refresh()
            if (model.banners.value == null) {
                model.getBanners()
            }
        }

        menuItem.setOnMenuItemClickListener {
            false
        }

        initToolbar()
        initRecyclerView()
        initBanner()

    }

    private fun initToolbar() {
        binding.collapsingToolbarLayout.setExpandedTitleColor(resources.getColor(R.color.transparent))
        binding.collapsingToolbarLayout.setCollapsedTitleTextColor(resources.getColor(R.color.white))

        model.appbarLayoutIsClosed.observe(viewLifecycleOwner) { isClosed ->
            menuItem.isVisible = isClosed
        }
    }

    private fun initBanner() {

        binding.viewPager2.apply {
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
            adapter = bannerAdapter
            LifecycleEventDispatcher(
                viewLifecycleOwner,
                onResume = { model.setCarousel(true) },
                onStop = { model.setCarousel(false) })
        }

        model.banners.observe(viewLifecycleOwner) {
            bannerAdapter.setData(it)
            bannerCurrentPosition = if (it.size > 1) 1 else 0
            model.bannerCurrentItem.value = bannerCurrentPosition to false
        }

    }

    private fun initRecyclerView() {

        binding.rvHome.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        binding.rvHome.layoutManager = LinearLayoutManager(requireContext())

        binding.rvHome.adapter =
            homeArticleAdapter.withLoadStateFooter(footer = ArticleLoadStateAdapter { homeArticleAdapter.retry() })
        homeArticleAdapter.addLoadStateListener { it ->
            // show empty list
            val isListEmpty = it.refresh is LoadState.NotLoading && homeArticleAdapter.itemCount == 0
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
                ).show()
            }
        }

        if (!model.article.hasObservers()) {
            model.article.observe(viewLifecycleOwner, {
                binding.refresh.isRefreshing = false
                lifecycleScope.launch { homeArticleAdapter.submitData(it) }
            })
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