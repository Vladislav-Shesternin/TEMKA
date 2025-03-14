package com.magicguru.aistrologer.game.actors

import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.utils.Align
import com.magicguru.aistrologer.game.utils.Acts
import com.magicguru.aistrologer.game.utils.SizeScaler
import com.magicguru.aistrologer.game.utils.actor.setBoundsScaled
import com.magicguru.aistrologer.game.utils.actor.setOrigin
import com.magicguru.aistrologer.game.utils.advanced.AdvancedGroup
import com.magicguru.aistrologer.game.utils.advanced.AdvancedScreen
import com.magicguru.aistrologer.game.utils.gdxGame

class ALoader(override val screen: AdvancedScreen) : AdvancedGroup() {

    override val sizeScaler = SizeScaler(SizeScaler.Axis.X, 250f)

    private val imgCircle1 = Image(gdxGame.assetsLoader.listCircle[0])
    private val imgCircle2 = Image(gdxGame.assetsLoader.listCircle[1])
    private val imgCircle3 = Image(gdxGame.assetsLoader.listCircle[2])

    override fun addActorsOnGroup() {
        addImgCircles()
    }

    // Actors ------------------------------------------------------------------------

    private fun addImgCircles() {
        addActors(imgCircle1, imgCircle2, imgCircle3)
        imgCircle1.apply {
            setBoundsScaled(sizeScaler, 0f, 0f, 250f, 250f)
            setOrigin(Align.center)
            addAction(Acts.forever(Acts.rotateBy(-360f, 3.5f)))

            addAction(Acts.forever(
                Acts.sequence(
                    Acts.scaleTo(1.2f, 1.2f, 0.7f),
                    Acts.scaleTo(1f, 1f, 0.7f),
                )
            ))
        }
        imgCircle2.apply {
            setBoundsScaled(sizeScaler, 33f, 30f, 186f, 186f)
            setOrigin(Align.center)
            addAction(Acts.forever(Acts.rotateBy(360f, 4f)))
        }
        imgCircle3.apply {
            setBoundsScaled(sizeScaler, 51f, 51f, 147f, 147f)
            setOrigin(Align.center)
            addAction(Acts.forever(Acts.rotateBy(-360f, 5f)))

            addAction(Acts.forever(
                Acts.sequence(
                    Acts.scaleTo(0.8f, 0.8f, 0.7f),
                    Acts.scaleTo(1f, 1f, 0.7f),
                )
            ))
        }
    }

}