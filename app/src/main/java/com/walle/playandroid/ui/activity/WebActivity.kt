package com.walle.playandroid.ui.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.load.model.stream.MediaStoreImageThumbLoader
import com.walle.playandroid.R
import com.walle.playandroid.databinding.ActivityWebBinding
import com.walle.playandroid.viewmodel.WebViewModel
import retrofit2.http.Url
import java.net.URL

class WebActivity : AppCompatActivity() {
    var currentUrl: String? = null
    private lateinit var binding: ActivityWebBinding
    private val model: WebViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_web)
        binding.data = model
        binding.lifecycleOwner = this
        init()
    }

    fun init() {
        binding.toolbar.setNavigationOnClickListener {
            if (binding.web.canGoBack()) {
                binding.web.goBack()
            } else {
                finish()
            }
        }
        initWeb()
        currentUrl = intent.getStringExtra("url")
        currentUrl?.let {
            binding.web.loadUrl(it)
        }

        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_web_collect -> {}
                R.id.menu_web_open_in_browser -> {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(currentUrl)))
                }
            }
            true
        }

    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWeb() {
        binding.web.run {
            settings.run {
                javaScriptEnabled = true
                useWideViewPort = true
                loadWithOverviewMode = true
                setSupportZoom(true)
                builtInZoomControls = true
                displayZoomControls = false
                cacheMode = WebSettings.LOAD_NO_CACHE
                domStorageEnabled = false
                defaultTextEncodingName = "UTF-8"
            }
            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                    Log.d("TAG", "shouldOverrideUrlLoading: $url")
                    url?.let {

                        if (it.startsWith("http")) {
                            currentUrl = it
                            return false
                        } else {
                            if (!it.contains("imeituan://|tbopen://|openapp.jdmobile://".toRegex())) {
                                try {
                                    val scheme = it.split("scheme=").last()
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(Uri.decode(scheme)))
                                    intent.flags = Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT
                                    startActivity(intent)
                                    return true
                                } catch (e: Exception) {
                                }
                            }
                        }


                    }

                    return false
                }
            }
            webChromeClient = object : WebChromeClient() {
                override fun onReceivedTitle(view: WebView?, title: String?) {
                    super.onReceivedTitle(view, title)
                    title?.let { binding.toolbar.title = it }
                }

                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                    super.onProgressChanged(view, newProgress)
                    model.progress.set(newProgress)
                }
            }
        }
    }

    companion object {
        fun actionStart(context: Context, url: String?) {

            val intent = Intent(context, WebActivity::class.java)
            intent.putExtra("url", url)
            context.startActivity(intent)
        }
    }

}