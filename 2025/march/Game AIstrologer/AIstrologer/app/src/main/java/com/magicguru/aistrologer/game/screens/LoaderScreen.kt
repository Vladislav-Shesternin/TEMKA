package com.magicguru.aistrologer.game.screens

import com.badlogic.gdx.math.Vector2
import com.magicguru.aistrologer.game.actors.ABackground
import com.magicguru.aistrologer.game.actors.main.AMainLoader
import com.magicguru.aistrologer.game.manager.MusicManager
import com.magicguru.aistrologer.game.manager.ParticleEffectManager
import com.magicguru.aistrologer.game.manager.SoundManager
import com.magicguru.aistrologer.game.manager.SpriteManager
import com.magicguru.aistrologer.game.utils.*
import com.magicguru.aistrologer.game.utils.actor.animHide
import com.magicguru.aistrologer.game.utils.advanced.AdvancedScreen
import com.magicguru.aistrologer.game.utils.advanced.AdvancedStage
import com.magicguru.aistrologer.util.log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class LoaderScreen : AdvancedScreen() {

    private val progressFlow     = MutableStateFlow(0f)
    private var isFinishLoading  = false
    private var isFinishProgress = false

    private val aBackground by lazy { ABackground(this, gdxGame.assetsLoader.BACKGROUND_BLUR) }
    private val aMain       by lazy { AMainLoader(this) }

    override fun show() {
        topViewportUI.project(Vector2(0f, 1667f)).also { topPos ->
            gdxGame.activity.webViewHelper.topY = topPos.y.toInt()
        }
        topViewportUI.project(Vector2(0f, 1219f)).also { topPos ->
            gdxGame.activity.webViewHelper.topY_Dialog = topPos.y.toInt()
        }

        loadSplashAssets()
        super.show()
        //setBackBackground(gdxGame.assetsLoader.BACKGROUND.region)
        loadAssets()
        collectProgress()
    }

    override fun render(delta: Float) {
        super.render(delta)
        loadingAssets()
        isFinish()
    }

    override fun hideScreen(block: Runnable) {
        aMain.animHide(TIME_ANIM_SCREEN) {
            block.run()
        }
    }

    override fun AdvancedStage.addActorsOnStageBack() {
        addBackground()
    }

    override fun AdvancedStage.addActorsOnStageUI() {
        addMain()
    }

    // Actors Back------------------------------------------------------------------------

    private fun AdvancedStage.addBackground() {
        addActor(aBackground)

        val screenRatio = viewportBack.screenWidth / viewportBack.screenHeight
        val imageRatio  = (WIDTH_UI / HEIGHT_UI)

        val scale = if (screenRatio > imageRatio) WIDTH_UI / viewportBack.screenWidth else HEIGHT_UI / viewportBack.screenHeight
        aBackground.setSize(WIDTH_UI / scale, HEIGHT_UI / scale)

        //aBackground.animToNewTexture(gdxGame.assetsLoader.BACKGROUND_BLUR, TIME_ANIM_SCREEN)
        //gdxGame.currentBackground = gdxGame.assetsLoader.BACKGROUND_BLUR
    }

    // Actors UI------------------------------------------------------------------------

    private fun AdvancedStage.addMain() {
        addAndFillActor(aMain)
    }

    // Logic ------------------------------------------------------------------------

    private fun loadSplashAssets() {
        with(gdxGame.spriteManager) {
            loadableAtlasList = mutableListOf(SpriteManager.EnumAtlas.LOADER.data)
            loadAtlas()
            loadableTexturesList = mutableListOf(SpriteManager.EnumTexture.LOADER_BACKGROUND_BLUR.data)
            loadTexture()
        }
        gdxGame.assetManager.finishLoading()
        gdxGame.spriteManager.initAtlasAndTexture()
    }

    private fun loadAssets() {
        with(gdxGame.spriteManager) {
            loadableAtlasList = SpriteManager.EnumAtlas.entries.map { it.data }.toMutableList()
            loadAtlas()
            loadableTexturesList = SpriteManager.EnumTexture.entries.map { it.data }.toMutableList()
            loadTexture()
        }
        with(gdxGame.musicManager) {
            loadableMusicList = MusicManager.EnumMusic.entries.map { it.data }.toMutableList()
            load()
        }
        with(gdxGame.soundManager) {
            loadableSoundList = SoundManager.EnumSound.entries.map { it.data }.toMutableList()
            load()
        }
        with(gdxGame.particleEffectManager) {
            loadableParticleEffectList = ParticleEffectManager.EnumParticleEffect.entries.map { it.data }.toMutableList()
            load()
        }
    }

    private fun initAssets() {
        gdxGame.spriteManager.initAtlasAndTexture()
        gdxGame.musicManager.init()
        gdxGame.soundManager.init()
        gdxGame.particleEffectManager.init()
    }

    private fun loadingAssets() {
        if (isFinishLoading.not()) {
            if (gdxGame.assetManager.update(16)) {
                isFinishLoading = true
                initAssets()
            }
            progressFlow.value = gdxGame.assetManager.progress
        }
    }

    private fun collectProgress() {
        coroutine?.launch {
            var progress = 0
            progressFlow.collect { p ->
                while (progress < (p * 100)) {
                    progress += 1
                    //runGDX { aMain.aLoader.setPercent(progress) }
                    if (progress % 50 == 0) log("progress = $progress%")
                    if (progress == 100) isFinishProgress = true

                    //delay((20..65).shuffled().first().toLong())
                }
            }
        }
    }

    private fun isFinish() {
        if (isFinishProgress /*&& GLOBAL_isGame*/) {
            isFinishProgress = false

            toGame()
        }
    }

    private fun toGame() {
        //gdxGame.activity.hideWebView()

//        gdxGame.musicUtil.apply { music = serious.apply {
//            isLooping = true
//            coff      = 0.20f
//        } }

        MusicPlayer().startPlayMusic()

        gdxGame.currentBackground = gdxGame.assetsLoader.BACKGROUND_BLUR

        if (gdxGame.mapUser[0] != null) {
            hideScreen { gdxGame.navigationManager.navigate(GameScreen::class.java.name, InputScreen::class.java.name) }
        } else {
            hideScreen { gdxGame.navigationManager.navigate(InputScreen::class.java.name) }
        }
    }


}