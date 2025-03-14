package com.magicguru.aistrologer.game.actors

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane
import com.badlogic.gdx.utils.Align
import com.magicguru.aistrologer.game.utils.GameColor
import com.magicguru.aistrologer.game.utils.advanced.AdvancedGroup
import com.magicguru.aistrologer.game.utils.advanced.AdvancedScreen
import com.magicguru.aistrologer.game.utils.font.FontParameter
import com.magicguru.aistrologer.game.utils.gdxGame

class APanelAnswer(override val screen: AdvancedScreen) : AdvancedGroup() {

    private val parameter = FontParameter().setCharacters(FontParameter.CharType.ALL)
    private val font58    = screen.fontGenerator_Ubuntu_Regular.generateFont(parameter.setSize(58))
    private val font40    = screen.fontGenerator_Ubuntu_Regular.generateFont(parameter.setBorder(1f, GameColor.purple_55).setSize(40))

    private val ls58 = LabelStyle(font58, Color.WHITE)
    private val ls40 = LabelStyle(font40, Color.WHITE)

    private val imgInput  = Image(gdxGame.assetsAll.input_9)
    private val lblTitle  = Label("Твой Астролог отвечает:", ls58)
    private val lblAnswer = Label("", ls40)
    private val scroll    = ScrollPane(lblAnswer)

    override fun addActorsOnGroup() {
        addAndFillActor(imgInput)
        addLblTitle()
        addLblAnswer()
    }

    // Actors ------------------------------------------------------------------------

    private fun addLblTitle() {
        addActor(lblTitle)
        lblTitle.setBounds(21f, 967f, 893f, 84f)
        lblTitle.setAlignment(Align.center)
    }

    private fun addLblAnswer() {
        addActor(scroll)
        scroll.setBounds(44f, 54f, 848f, 833f)

        lblAnswer.setSize(848f, 833f)
        lblAnswer.setAlignment(Align.center)
        lblAnswer.wrap = true
    }

    // Logic -------------------------------------------------------------------------------

    fun setAnswer(answer: String) {
        lblAnswer.setText(answer)
    }

    fun reset() {
        lblAnswer.setText("")
    }

}