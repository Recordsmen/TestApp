package com.example.testapp.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.KeyEvent
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.testapp.databinding.ActivityWebviewBinding

const val SITE_URL = "https://www.timeanddate.com/weather/ukraine/kyiv/ext"

class WebViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWebviewBinding

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWebviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            wvInternet.loadUrl(SITE_URL)
            wvInternet.settings.javaScriptEnabled = true
            wvInternet.webViewClient = WebViewClient()
            wvInternet.webChromeClient = object : WebChromeClient() {
                override fun onProgressChanged(view: WebView, progress: Int) {
                    binding.progressBar.progress = progress
                    if (progress == 100){
                        binding.progressBar.isVisible = false
                    }
                }
            }
        }
    }
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && binding.wvInternet.canGoBack()) {
            binding.wvInternet.goBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
}