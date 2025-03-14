package com.magicguru.aistrologer.game.actors

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.Align
import com.magicguru.aistrologer.game.utils.Acts
import com.magicguru.aistrologer.game.utils.actor.animHide
import com.magicguru.aistrologer.game.utils.actor.animShow
import com.magicguru.aistrologer.game.utils.advanced.AdvancedGroup
import com.magicguru.aistrologer.game.utils.advanced.AdvancedScreen

class ABackground(
    override val screen: AdvancedScreen,
    val texture: Texture
): AdvancedGroup() {

   private val imgOriginal = Image(texture)
   private val imgTmp      = Image(texture)


    override fun addActorsOnGroup() {
        imgTmp.color.a = 0f
        addAndFillActors(imgOriginal, imgTmp)

        setOrigin(Align.center)
        addAction(Acts.forever(Acts.sequence(
            Acts.scaleTo(1.009f, 1.009f, 1f),
            Acts.scaleTo(1f, 1f, 1f),
        )))
    }

    // Logic -------------------------------------------------------------------------

    fun animToNewTexture(texture: Texture, time: Float, blockEnd: Runnable = Runnable {}) {
        if (this.texture == texture) return

        val newDrawable = TextureRegionDrawable(texture)
        imgTmp.drawable = newDrawable

        imgOriginal.clearActions()
        imgTmp.clearActions()

        imgOriginal.animHide(time) { imgOriginal.drawable = newDrawable }
        imgTmp.animShow(time + 0.1f) {
            imgOriginal.color.a = 1f
            imgTmp.color.a = 0f
            blockEnd.run()
        }
    }

}