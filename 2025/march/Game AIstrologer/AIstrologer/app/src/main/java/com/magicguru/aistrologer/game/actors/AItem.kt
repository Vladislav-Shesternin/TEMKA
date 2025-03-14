package com.magicguru.aistrologer.game.actors

import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.utils.Align
import com.magicguru.aistrologer.game.utils.Acts
import com.magicguru.aistrologer.game.utils.SizeScaler
import com.magicguru.aistrologer.game.utils.actor.setBoundsScaled
import com.magicguru.aistrologer.game.utils.advanced.AdvancedGroup
import com.magicguru.aistrologer.game.utils.advanced.AdvancedScreen
import com.magicguru.aistrologer.game.utils.gdxGame

class AItem(override val screen: AdvancedScreen) : AdvancedGroup() {

    private val imgBackForItem = Image(gdxGame.assetsAll.BACK_FOR_ITEM)
    private val imgItem        = Image(gdxGame.assetsAll.listItems[gdxGame.zodiacIndex])

    override fun addActorsOnGroup() {
        addImgCircles()
    }

    // Actors ------------------------------------------------------------------------

    private fun addImgCircles() {
        addAndFillActor(imgBackForItem)
        addActor(imgItem)
        imgItem.setBounds(31f, 30f, 400f, 400f)

        imgItem.setOrigin(Align.center)
        imgItem.addAction(Acts.forever(Acts.sequence(
            Acts.scaleTo(0.93f, 0.93f, 0.8f, Interpolation.fastSlow),
            Acts.scaleTo(1f, 1f, 1f, Interpolation.sine),
        )))
    }

}