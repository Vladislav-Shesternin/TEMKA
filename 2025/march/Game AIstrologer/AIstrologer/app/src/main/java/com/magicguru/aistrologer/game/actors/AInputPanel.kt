package com.magicguru.aistrologer.game.actors

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.utils.Align
import com.magicguru.aistrologer.game.screens.InputScreen
import com.magicguru.aistrologer.game.utils.GLOBAL_listTitle
import com.magicguru.aistrologer.game.utils.advanced.AdvancedGroup
import com.magicguru.aistrologer.game.utils.advanced.AdvancedScreen
import com.magicguru.aistrologer.game.utils.font.FontParameter
import com.magicguru.aistrologer.game.utils.gdxGame

class AInputPanel(override val screen: AdvancedScreen) : AdvancedGroup() {

    private val firstText = "Введите "

    private val parameter = FontParameter().setCharacters(FontParameter.CharType.ALL)
    private val font58    = screen.fontGenerator_Ubuntu_Regular.generateFont(parameter.setSize(58))

    private val ls58 = LabelStyle(font58, Color.WHITE)

    private val imgInput = Image(gdxGame.assetsAll.input_9)
    private val lblTitle = Label(firstText + "Ваше " + GLOBAL_listTitle[InputScreen.CURRENT_INDEX], ls58)
    private val inputT   = AInputText_1(screen)

    var blockInputText: (String) -> Unit = {}

    override fun addActorsOnGroup() {
        addAndFillActor(imgInput)
        addLblTitle()
        addInputText()
    }

    // Actors ------------------------------------------------------------------------

    private fun addLblTitle() {
        addActor(lblTitle)
        lblTitle.setBounds(22f, 207f, 893f, 84f)
        lblTitle.setAlignment(Align.center)
    }

    private fun addInputText() {
        addActor(inputT)
        inputT.setBounds(22f, 48f, 893f, 84f)

        inputT.blockTextFieldListener = { text -> blockInputText(text) }
    }

    // Logic -------------------------------------------------------------------------------

    fun update(isFocus: Boolean = false) {
        inputT.textField.text = ""
        blockInputText("")
        lblTitle.setText(firstText + GLOBAL_listTitle[InputScreen.CURRENT_INDEX])

        if (isFocus) inputT.showCursor()
    }

    fun setDate(text: String) {
        inputT.textField.text = text
    }


}