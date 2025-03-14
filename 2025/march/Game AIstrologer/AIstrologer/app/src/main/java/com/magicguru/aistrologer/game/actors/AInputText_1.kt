package com.magicguru.aistrologer.game.actors

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input.OnscreenKeyboardType
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.TextField
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.Align
import com.magicguru.aistrologer.game.utils.Acts
import com.magicguru.aistrologer.game.utils.advanced.AdvancedGroup
import com.magicguru.aistrologer.game.utils.advanced.AdvancedScreen
import com.magicguru.aistrologer.game.utils.font.FontParameter
import com.magicguru.aistrologer.game.utils.gdxGame
import com.magicguru.aistrologer.util.log

class AInputText_1(
    override val screen: AdvancedScreen,
    private val onscreenKeyboardType: OnscreenKeyboardType = OnscreenKeyboardType.Default,
): AdvancedGroup() {

    private val parameter = FontParameter().setCharacters(FontParameter.CharType.ALL)
    private val font58    = screen.fontGenerator_Ubuntu_Regular.generateFont(parameter.setSize(58))

    private val textFieldStyle = TextField.TextFieldStyle().apply {
            font       = font58
            fontColor  = Color.WHITE
            cursor     = TextureRegionDrawable(screen.drawerUtil.getTexture(Color.WHITE))
            selection  = TextureRegionDrawable(screen.drawerUtil.getTexture(Color.BLACK.cpy().apply { a = 0.25f }))
        }

    val textField = TextField("", textFieldStyle)

    private val imgCursor = Image(screen.drawerUtil.getTexture(Color.WHITE))

    var blockTextFieldListener: (String) -> Unit = {}

    override fun addActorsOnGroup() {
        addTextField()
        addImgCursor()
    }

    private fun addImgCursor() {
        addActor(imgCursor)
        imgCursor.setPosition(
            (width / 2) - 1.5f,
            (height / 2) - 25f,
        )
        imgCursor.setSize(3f, 50f)

        imgCursor.isVisible = false
        imgCursor.addAction(Acts.forever(Acts.sequence(
            Acts.fadeOut(0.2f),
            Acts.delay(0.3f),
            Acts.fadeIn(0.2f),
        )))
    }

    private fun addTextField() {
        addAndFillActor(textField)

        textField.alignment = Align.center

        textField.setOnscreenKeyboard { visible ->
            Gdx.input.setOnscreenKeyboardVisible(visible, onscreenKeyboardType)
        }

        textField.setTextFieldListener { _, key ->
            blockTextFieldListener(textField.text)

            if (key == '\n' || key == '\r') { // Перевіряємо Enter або Return
                stage?.keyboardFocus = null
                Gdx.input.setOnscreenKeyboardVisible(false)
            }

            imgCursor.isVisible = textField.text.isBlank()
        }
        textField.addListener(object : FocusListener() {
            override fun keyboardFocusChanged(event: FocusEvent?, actor: Actor?, focused: Boolean) {
                imgCursor.isVisible = focused
            }
        })
    }

    fun showCursor() {
        imgCursor.isVisible = true
    }

}