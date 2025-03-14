package com.magicguru.aistrologer.game.actors

import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.utils.Align
import com.magicguru.aistrologer.game.actors.button.AButton
import com.magicguru.aistrologer.game.screens.InputScreen
import com.magicguru.aistrologer.game.utils.GLOBAL_listTitle
import com.magicguru.aistrologer.game.utils.GameColor
import com.magicguru.aistrologer.game.utils.advanced.AdvancedGroup
import com.magicguru.aistrologer.game.utils.advanced.AdvancedScreen
import com.magicguru.aistrologer.game.utils.font.FontParameter
import com.magicguru.aistrologer.game.utils.gdxGame

class ATop(override val screen: AdvancedScreen) : AdvancedGroup() {

    private val parameter = FontParameter().setCharacters(FontParameter.CharType.ALL)
    private val font58    = screen.fontGenerator_Ubuntu_Regular.generateFont(parameter.setSize(58))
    private val font35    = screen.fontGenerator_Ubuntu_Regular.generateFont(parameter.setSize(35))

    private val ls58 = LabelStyle(font58, GameColor.background)
    private val ls35 = LabelStyle(font35, GameColor.background)

    private val imgTop   = Image(gdxGame.assetsAll.top)
    private val btnBack  = AButton(screen, AButton.Type.Back)
    private val lblTitle = Label(GLOBAL_listTitle[InputScreen.CURRENT_INDEX], ls58)
    private val lblCount = Label("${InputScreen.CURRENT_INDEX.inc()}/3", ls35)

    var blockBack = {}

    override fun addActorsOnGroup() {
        addAndFillActor(imgTop)
        addLblTitle()
        addLblCount()
        addBtnBack()
    }

    // Actors ------------------------------------------------------------------------

    private fun addBtnBack() {
        addActor(btnBack)
        btnBack.apply {
            setBounds(0f, 0f, 147f, 100f)
            setOnClickListener {
                blockBack()
            }
        }
    }

    private fun addLblTitle() {
        addActor(lblTitle)
        lblTitle.setBounds(0f, 8f, 936f, 84f)
        lblTitle.setAlignment(Align.center)
    }

    private fun addLblCount() {
        addActor(lblCount)
        lblCount.setBounds(857f, 24f, 58f, 51f)
        lblCount.setAlignment(Align.right)
    }

    // Logic -------------------------------------------------------------------------------

    fun update() {
        lblTitle.setText(GLOBAL_listTitle[InputScreen.CURRENT_INDEX])
        lblCount.setText("${InputScreen.CURRENT_INDEX.inc()}/3")
    }


}