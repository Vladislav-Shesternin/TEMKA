package com.magicguru.aistrologer.game.screens

import com.magicguru.aistrologer.game.actors.ABackground
import com.magicguru.aistrologer.game.actors.main.AMainGame
import com.magicguru.aistrologer.game.utils.HEIGHT_UI
import com.magicguru.aistrologer.game.utils.TIME_ANIM_SCREEN
import com.magicguru.aistrologer.game.utils.WIDTH_UI
import com.magicguru.aistrologer.game.utils.advanced.AdvancedMainScreen
import com.magicguru.aistrologer.game.utils.advanced.AdvancedStage
import com.magicguru.aistrologer.game.utils.gdxGame
import com.magicguru.aistrologer.util.log

class GameScreen: AdvancedMainScreen() {

    private val aBackground = ABackground(this, gdxGame.currentBackground)
    //private val effectFallingLeaves = AParticleEffectActor(ParticleEffect(gdxGame.particleEffectUtil.FallingLeaves), false)

    override val aMain = AMainGame(this)

    override fun AdvancedStage.addActorsOnStageBack() {
        addBackground()
        addEffectLeaf()
    }

    override fun AdvancedStage.addActorsOnStageUI() {
        addMain()
    }

    override fun hideScreen(block: Runnable) {
        aMain.animHideMain { block.run() }
    }

    // Actors Back------------------------------------------------------------------------

    private fun AdvancedStage.addBackground() {
        addActor(aBackground)

        val screenRatio = viewportBack.screenWidth / viewportBack.screenHeight
        val imageRatio  = (WIDTH_UI / HEIGHT_UI)

        val scale = if (screenRatio > imageRatio) WIDTH_UI / viewportBack.screenWidth else HEIGHT_UI / viewportBack.screenHeight
        aBackground.setSize(WIDTH_UI / scale, HEIGHT_UI / scale)

        aBackground.animToNewTexture(gdxGame.assetsAll.BACKGROUND_DEFF, TIME_ANIM_SCREEN)
        gdxGame.currentBackground = gdxGame.assetsAll.BACKGROUND_DEFF
    }

    private fun AdvancedStage.addEffectLeaf() {
        val yPercent_20 = (viewportBack.screenHeight * 0.2f)
        val scale       = (viewportBack.screenWidth / 1080f)

        // effectFallingLeaves.particleEffect.scaleEffect(scale)
        // effectFallingLeaves.y = yPercent_20
        // addActor(effectFallingLeaves)
        // effectFallingLeaves.start()
    }

    // Actors UI------------------------------------------------------------------------

    override fun AdvancedStage.addMain() {
        addAndFillActor(aMain)
    }

}