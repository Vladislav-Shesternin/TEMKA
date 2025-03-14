package com.magicguru.aistrologer.game.screens

import com.magicguru.aistrologer.game.actors.ABackground
import com.magicguru.aistrologer.game.actors.main.AMainInput
import com.magicguru.aistrologer.game.actors.shader.ANewBlurGroup
import com.magicguru.aistrologer.game.utils.HEIGHT_UI
import com.magicguru.aistrologer.game.utils.TIME_ANIM_SCREEN
import com.magicguru.aistrologer.game.utils.WIDTH_UI
import com.magicguru.aistrologer.game.utils.advanced.AdvancedMainScreen
import com.magicguru.aistrologer.game.utils.advanced.AdvancedStage
import com.magicguru.aistrologer.game.utils.gdxGame
import com.magicguru.aistrologer.game.utils.region

class InputScreen: AdvancedMainScreen() {

    companion object {
        var CURRENT_INDEX = 0
    }

    private val aBackground = ABackground(this, gdxGame.currentBackground)

    override val aMain = AMainInput(this)

    override fun AdvancedStage.addActorsOnStageBack() {
        //setBackBackground(gdxGame.assetsAll.BACKGROUND_DEFF.region)
        addBackground()
    }

    override fun AdvancedStage.addActorsOnStageUI() {
        addMain()
    }

    override fun hideScreen(block: Runnable) {
        aMain.animHideMain { block.run() }
    }

    override fun dispose() {
        CURRENT_INDEX = 0
        super.dispose()
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

    // Actors UI------------------------------------------------------------------------

    override fun AdvancedStage.addMain() {
        addAndFillActor(aMain)
    }

}