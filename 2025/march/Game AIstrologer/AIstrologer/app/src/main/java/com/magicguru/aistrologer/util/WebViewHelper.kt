package com.magicguru.aistrologer.util

import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.addCallback
import androidx.core.view.isVisible
import com.magicguru.aistrologer.MainActivity

class WebViewHelper(val activity: MainActivity) {

    private var isOffer = true

    var blockBack    : () -> Unit = {}
    var blockRedirect: () -> Unit = {}

    @Suppress("DEPRECATION")
    fun initWeb() {
        activity.runOnUiThread {
            activity.binding.webView.apply {
                settings.apply {
                    allowFileAccessFromFileURLs = true
                    allowContentAccess = true
                    javaScriptEnabled = listOf(true).first()
                    javaScriptCanOpenWindowsAutomatically = true
                    allowFileAccess = true
                    mixedContentMode = 0
                    useWideViewPort = true
                    allowUniversalAccessFromFileURLs = true
                    loadWithOverviewMode = true
                    domStorageEnabled = true
                    databaseEnabled = true
                }

                webViewClient = WebClient()

                // Реєструємо зворотний виклик у диспетчері
                activity.onBackPressedDispatcher.addCallback {
                    if (canGoBack()) {
                        goBack()
                    } else {
                        if (isOffer.not() && isVisible) {
                            activity.hideWebView()
                        } else blockBack()
                    }
                }
            }
        }
    }

    fun showUrl(url: String, isOffer: Boolean = true) {
        activity.runOnUiThread {
            log("showUrl: $url | isOffer = $isOffer")

            this.isOffer = isOffer
            activity.binding.webView.loadUrl(url)

            activity.showWebView()
        }
    }

    private inner class WebClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {

            log("redirect: ${request?.url.toString()}")

            if (request?.url.toString().contains("https://lmmjg")) {
                log("contains")
                activity.hideWebView()
                blockRedirect()
                return true
            }

            return false
        }
    }

}