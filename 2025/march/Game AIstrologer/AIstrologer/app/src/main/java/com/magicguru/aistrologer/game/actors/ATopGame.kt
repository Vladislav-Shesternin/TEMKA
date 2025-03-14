package com.magicguru.aistrologer.game.actors

import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.utils.Align
import com.magicguru.aistrologer.game.actors.button.AButton
import com.magicguru.aistrologer.game.utils.GameColor
import com.magicguru.aistrologer.game.utils.advanced.AdvancedGroup
import com.magicguru.aistrologer.game.utils.advanced.AdvancedScreen
import com.magicguru.aistrologer.game.utils.font.FontParameter
import com.magicguru.aistrologer.game.utils.gdxGame

class ATopGame(override val screen: AdvancedScreen) : AdvancedGroup() {

    private val dataUser = gdxGame.ds_UserData.flow.value

    private val parameter = FontParameter().setCharacters(FontParameter.CharType.ALL)
    private val font58    = screen.fontGenerator_Ubuntu_Regular.generateFont(parameter.setSize(58))
    private val font25    = screen.fontGenerator_Ubuntu_Regular.generateFont(parameter.setSize(25))

    private val ls58 = LabelStyle(font58, GameColor.background)
    private val ls25 = LabelStyle(font25, GameColor.blue_06)

    private val imgTop   = Image(gdxGame.assetsAll.top)
    private val imgDate  = Image(gdxGame.assetsAll.date)
    private val btnBack  = AButton(screen, AButton.Type.Back)
    private val lblTitle = Label("Привет ${dataUser.uName}", ls58)
    private val lblDate  = Label(dataUser.dateOfBirth, ls25)

    override fun addActorsOnGroup() {
        addActor(imgTop)
        imgTop.setBounds(0f, 53f, 936f, 100f)
        addActor(imgDate)
        imgDate.setBounds(350f, 0f, 236f, 53f)

        addLblTitle()
        addLblDate()
        addBtnBack()
    }

    // Actors ------------------------------------------------------------------------

    private fun addBtnBack() {
        addActor(btnBack)
        btnBack.apply {
            setBounds(0f, 53f, 147f, 100f)
            setOnClickListener {
                screen.hideScreen { gdxGame.navigationManager.back() }
            }
        }
    }

    private fun addLblTitle() {
        addActor(lblTitle)
        lblTitle.setBounds(0f, 61f, 936f, 84f)
        lblTitle.setAlignment(Align.center)
    }

    private fun addLblDate() {
        addActor(lblDate)
        lblDate.setBounds(350f, 0f, 236f, 53f)
        lblDate.setAlignment(Align.center)
    }

}