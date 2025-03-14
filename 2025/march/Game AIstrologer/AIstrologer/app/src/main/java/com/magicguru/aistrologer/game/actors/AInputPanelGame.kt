package com.magicguru.aistrologer.game.actors

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.utils.Align
import com.magicguru.aistrologer.game.actors.button.ACheckButton
import com.magicguru.aistrologer.game.utils.Acts
import com.magicguru.aistrologer.game.utils.GameColor
import com.magicguru.aistrologer.game.utils.actor.animHide
import com.magicguru.aistrologer.game.utils.actor.animShow
import com.magicguru.aistrologer.game.utils.actor.disable
import com.magicguru.aistrologer.game.utils.actor.enable
import com.magicguru.aistrologer.game.utils.advanced.AdvancedGroup
import com.magicguru.aistrologer.game.utils.advanced.AdvancedScreen
import com.magicguru.aistrologer.game.utils.font.FontParameter
import com.magicguru.aistrologer.game.utils.gdxGame

class AInputPanelGame(override val screen: AdvancedScreen) : AdvancedGroup() {

    private val parameter = FontParameter().setCharacters(FontParameter.CharType.ALL)
    private val font58    = screen.fontGenerator_Ubuntu_Regular.generateFont(parameter.setSize(58))
    private val font25    = screen.fontGenerator_Ubuntu_Regular.generateFont(parameter.setSize(25))
    private val font40    = screen.fontGenerator_Ubuntu_Regular.generateFont(parameter.setBorder(1f, GameColor.purple_55).setSize(40))

    private val ls58 = LabelStyle(font58, Color.WHITE)
    private val ls40 = LabelStyle(font40, Color.WHITE)
    private val ls25 = LabelStyle(font25, Color.WHITE)

    private val imgInput  = Image(gdxGame.assetsAll.input_9)
    private val lblTitle  = Label("Напишите Свой Вопрос", ls58)
    private val lblHint   = Label("Задайте вопрос астрологу...", ls40)
    private val lblCount  = Label("0/500", ls25)
    private val inputT    = AInputText_Question(screen)
    //private val btnCheck  = ACheckButton(screen)

    var blockInputText: (String) -> Unit = {}

    override fun addActorsOnGroup() {
        addAndFillActor(imgInput)
        addLblTitle()
        addLblCount()
        addInputText()
        addLblHint()
        //addBtnCheck()
    }

    // Actors ------------------------------------------------------------------------

    private fun addLblTitle() {
        addActor(lblTitle)
        lblTitle.setBounds(22f, 207f, 893f, 84f)
        lblTitle.setAlignment(Align.center)
    }

    private fun addLblCount() {
        addActor(lblCount)
        lblCount.setBounds(22f, 157f, 893f, 36f)
        lblCount.setAlignment(Align.center)
        lblCount.disable()
    }

    private fun addLblHint() {
        addActor(lblHint)
        lblHint.setBounds(22f, 61f, 893f, 58f)
        lblHint.setAlignment(Align.center)
        lblHint.disable()
    }

    private fun addInputText() {
        addActor(inputT)
        inputT.setBounds(44f, 34f, 848f, 112f)

        inputT.blockTextFieldListener = { text ->
            blockInputText(text)

            if (text.count() > 0) {
                if (lblHint.color.a == 1f) lblHint.animHide(0.2f)
            }

            lblCount.setText("${text.count()}/500")
        }
    }

//    private fun addBtnCheck() {
//        addActor(btnCheck)
//        btnCheck.setBounds(792f, 169f, 144f, 144f)
//        btnCheck.disable()
//        btnCheck.color.a = 0f
//
//        btnCheck.blockClick = {
//            screen.stageUI.hideKeyboard()
//        }
//    }

    // Logic -------------------------------------------------------------------------------

    fun showKeyboard(time: Float) {
        addAction(Acts.sizeTo(936f, 626f, time))
        imgInput.addAction(Acts.sizeTo(936f, 626f, time))
        lblHint.animHide(0.2f)
        inputT.addAction(Acts.sizeTo(848f, 373f, time))
        //btnCheck.addAction(Acts.moveTo(793f, 458f, time))
        lblTitle.addAction(Acts.moveTo(22f, 496f, time))
        lblCount.addAction(Acts.moveTo(22f, 447f, time))
        lblCount.animShow(time)

        //btnCheck.animShow(0.25f) { btnCheck.enable() }
    }

    fun hideKeyboard(time: Float, blockEmpty: () -> Unit) {
        stage?.unfocus(inputT)
        if (inputT.textArea.text.isBlank()) {
            lblHint.animShow(0.2f)

            addAction(Acts.sizeTo(936f, 337f, time))
            imgInput.addAction(Acts.sizeTo(936f, 337f, time))
            inputT.addAction(Acts.sizeTo(848f, 90f, time))
            //btnCheck.addAction(Acts.moveTo(792f, 169f, time))
            lblTitle.addAction(Acts.moveTo(22f, 207f, time))
            lblCount.addAction(Acts.moveTo(21f, 157f, time))
            lblCount.animHide(time)

            blockEmpty()
        }

        //btnCheck.animHide(0.25f) { btnCheck.disable() }
    }

    fun resetPanel(time: Float) {
        inputT.textArea.text = ""
        lblHint.animShow(0.2f)

        addAction(Acts.sizeTo(936f, 337f, time))
        imgInput.addAction(Acts.sizeTo(936f, 337f, time))
        inputT.addAction(Acts.sizeTo(848f, 90f, time))
        //btnCheck.addAction(Acts.moveTo(792f, 169f, time))
        lblTitle.addAction(Acts.moveTo(22f, 207f, time))
        lblCount.addAction(Acts.moveTo(21f, 157f, time))
        lblCount.animHide(time)

        //btnCheck.animHide(0.25f) { btnCheck.disable() }
    }

}