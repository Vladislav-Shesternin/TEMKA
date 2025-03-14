package com.magicguru.aistrologer.game.actors.button

import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.utils.Align
import com.magicguru.aistrologer.game.utils.actor.disable
import com.magicguru.aistrologer.game.utils.advanced.AdvancedScreen

open class ATextButton(
    override val screen: AdvancedScreen,
    text: String,
    labelStyle: Label.LabelStyle,
    type: Type = Type.Gradient,
) : AButton(screen, type) {

    val label = Label(text, labelStyle)

    override fun addActorsOnGroup() {
        super.addActorsOnGroup()

        addAndFillActor(label)

        label.disable()
        label.setAlignment(Align.center)
    }

}