package com.magicguru.aistrologer.game.actors

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.utils.Align
import com.magicguru.aistrologer.game.actors.label.ALabel
import com.magicguru.aistrologer.game.actors.shader.AMaskGroup
import com.magicguru.aistrologer.game.utils.Acts
import com.magicguru.aistrologer.game.utils.GLOBAL_listZodiacName
import com.magicguru.aistrologer.game.utils.actor.disable
import com.magicguru.aistrologer.game.utils.advanced.AdvancedGroup
import com.magicguru.aistrologer.game.utils.advanced.AdvancedScreen
import com.magicguru.aistrologer.game.utils.font.FontParameter
import com.magicguru.aistrologer.game.utils.gdxGame
import com.magicguru.aistrologer.game.utils.getZodiacIndex

class AGoroscop(override val screen: AdvancedScreen) : AdvancedGroup() {

    private val listRouletteAngle = listOf(
        -30f * 0f,
        -30f * 1f,
        -30f * 2f,
        -30f * 3f,
        -30f * 4f,
        -30f * 5f,
        -30f * 6f,
        -30f * 7f,
        -30f * 8f,
        -30f * 9f,
        -30f * 10f,
        -30f * 11f,
    )

    private val parameter = FontParameter().setCharacters(FontParameter.CharType.ALL)
    private val font40    = screen.fontGenerator_Ubuntu_Regular.generateFont(parameter.setSize(40))

    private val ls40 = LabelStyle(font40, Color.WHITE)

    private val imgBackgr    = Image(gdxGame.assetsAll.BACK_FOR_ROULETTE)
    private val imgSelect    = Image(gdxGame.assetsAll.select)
    private val lblName      = ALabel(screen, GLOBAL_listZodiacName[gdxGame.zodiacIndex], ls40)
    private val imgRoulette  = Image(gdxGame.assetsAll.ROULETTE)
    private val mask         = AMaskGroup(screen)

    override fun addActorsOnGroup() {
        disable()
        addAndFillActor(imgBackgr)
        addSelect()
        addImgRoulette()
    }

    // Actors ------------------------------------------------------------------------

    private fun addSelect() {
        addActors(imgSelect, lblName)
        imgSelect.apply {
            setBounds(250f, 47f, 353f, 446f)
        }
        lblName.apply {
            setBounds(335f, 389f, 103f, 58f)
            setOrigin(Align.center)
            label.setAlignment(Align.center)
            rotation = 15f
        }
    }

    private fun addImgRoulette() {
        addAndFillActor(mask)
        mask.addActor(imgRoulette)
        imgRoulette.setBounds(24f, -471f, 948f, 948f)
        imgRoulette.setOrigin(Align.center)
        imgRoulette.rotation = listRouletteAngle[gdxGame.zodiacIndex]
    }

    // Logic -------------------------------------------------------------------------------

    fun update(date: String) {
        gdxGame.zodiacIndex = getZodiacIndex(date)
        lblName.label.setText(GLOBAL_listZodiacName[gdxGame.zodiacIndex])
        imgRoulette.addAction(Acts.rotateTo(listRouletteAngle[gdxGame.zodiacIndex], 1f, Interpolation.sine))
    }

}