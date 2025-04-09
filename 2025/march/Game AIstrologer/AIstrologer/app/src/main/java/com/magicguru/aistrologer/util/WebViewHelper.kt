package com.magicguru.aistrologer.util

import android.graphics.Outline
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.addCallback
import androidx.core.view.isVisible
import com.magicguru.aistrologer.MainActivity
import com.magicguru.aistrologer.game.GDX_GLOBAL_fullUrl
import com.magicguru.aistrologer.game.utils.gdxGame
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WebViewHelper(val activity: MainActivity) {

    var isRedirectToGame = false
        private set

    var topY        = 0
    var topY_Dialog = 0

    private var isOffer = true

    var blockBack    : () -> Unit = {}
    //var blockRedirect: () -> Unit = {}
    var blockRedirectThankKey: () -> Unit = {}
    var blockShowWebAd       : () -> Unit = {}

    @Suppress("DEPRECATION")
    fun initWeb() {
        activity.runOnUiThread {
            activity.binding.webView.apply {
                this.outlineProvider = object : ViewOutlineProvider() {
                    override fun getOutline(view: View, outline: Outline) {
                        val radius = 32f
                        outline.setRoundRect(0, 0, view.width, view.height /*+ 100*/, radius)
                    }
                }
                this.clipToOutline = true

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
                    mediaPlaybackRequiresUserGesture = false
                }

                webViewClient = WebClient()

                // Реєструємо зворотний виклик у диспетчері
                activity.onBackPressedDispatcher.addCallback {
                    log("go back")
                    if (canGoBack()) {
                        //goBack()
                    } else {
                        if (isOffer.not() && isVisible) {
                            //activity.hideWebView()
                            //blockHideWebView()
                        } else blockBack()
                    }
                }
            }
        }

        activity.coroutine.launch(Dispatchers.Default) {
            delay(2000)
            withContext(Dispatchers.Main) {
                log("web.y = $topY")
                activity.binding.webView.layoutParams = activity.binding.webView.layoutParams.apply {
                    height = topY - (15).toPx()
                }
            }
        }
    }

    fun loadUrl(url: String, isOffer: Boolean = true) {
        activity.coroutine.launch(Dispatchers.Main) {
            log("loadUrl: $url | isOffer = $isOffer")

            this@WebViewHelper.isOffer = isOffer
            activity.binding.webView.loadUrl(url)

            isRedirectToGame = false
        }
    }

    private inner class WebClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {

            log("redirect: ${request?.url.toString()}")

            isRedirectToGame = false

            when {
                request?.url.toString().contains("https://axudctq") -> {
                    log("contains: CLOSE go to Game")
                    isRedirectToGame = true
                    blockRedirectThankKey()
                    return true
                }
                request?.url.toString().contains("openchttrue") -> {
                    log("contains: redirectClickKey")
                    setFillParent()
                }
                request?.url.toString().contains("openthxpgtrue") -> {
                    log("contains: redirectThankKey")

                    GDX_GLOBAL_fullUrl = ""
                    gdxGame.sharedPreferences.edit().putBoolean("redirectThankKey", true).apply()

                    activity.coroutine.launch(Dispatchers.Main) {
                        delay(5000)
                        blockRedirectThankKey()
                    }
                }
            }

            return false
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            log("onPageFinished: url = $url")

            if (url.toString().contains("about:blank").not() && isRedirectToGame.not()) {
                log("onPageFinished showWebView url = $url")
                if (activity.binding.webView.isVisible.not()) {
                    blockShowWebAd()
                    activity.showWebView()
                }
            }
        }
    }

    fun updateHeight(newH: Int) {
        activity.runOnUiThread {
            activity.binding.webView.also { web ->
                web.layoutParams = web.layoutParams.apply {
                    height = newH - (15).toPx()
                }
            }
        }
    }

    private fun setFillParent() {
        activity.runOnUiThread {
            activity.binding.webView.also { web ->
                val params = web.layoutParams
                if (params is ViewGroup.MarginLayoutParams) {
                    params.setMargins(0, 0, 0, 0)
                }
                params.width = ViewGroup.LayoutParams.MATCH_PARENT
                params.height = ViewGroup.LayoutParams.MATCH_PARENT
                web.layoutParams = params
                web.requestLayout()
            }
        }
    }

}