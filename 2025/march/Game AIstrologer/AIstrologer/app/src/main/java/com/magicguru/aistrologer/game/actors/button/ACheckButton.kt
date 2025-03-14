package com.magicguru.aistrologer.game.actors.button

import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.utils.Align
import com.magicguru.aistrologer.game.utils.Acts
import com.magicguru.aistrologer.game.utils.actor.disable
import com.magicguru.aistrologer.game.utils.advanced.AdvancedGroup
import com.magicguru.aistrologer.game.utils.advanced.AdvancedScreen
import com.magicguru.aistrologer.game.utils.gdxGame

open class ACheckButton(override val screen: AdvancedScreen): AdvancedGroup() {

    private val imgBlum  = Image(gdxGame.assetsAll.blum)
    private val btnCheck = AButton(screen, AButton.Type.Check)

    var blockClick = {}

    override fun addActorsOnGroup() {
        addAndFillActor(imgBlum)
        imgBlum.setOrigin(Align.center)
        imgBlum.addAction(Acts.forever(Acts.sequence(
            Acts.scaleTo(0.8f, 0.8f, 0.6f),
            Acts.scaleTo(1f, 1f, 0.6f),
        )))

        addActor(btnCheck)
        btnCheck.setBounds(22f, 22f, 100f, 100f)
        btnCheck.setOnClickListener { blockClick() }
    }

}