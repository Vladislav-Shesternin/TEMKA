package com.magicguru.aistrologer.game.actors

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input.OnscreenKeyboardType
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.ui.TextArea
import com.badlogic.gdx.scenes.scene2d.ui.TextField
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.Align
import com.magicguru.aistrologer.game.utils.GameColor
import com.magicguru.aistrologer.game.utils.advanced.AdvancedGroup
import com.magicguru.aistrologer.game.utils.advanced.AdvancedScreen
import com.magicguru.aistrologer.game.utils.font.FontParameter

class AInputText_Question(override val screen: AdvancedScreen): AdvancedGroup() {

    private val parameter = FontParameter().setCharacters(FontParameter.CharType.ALL)
    private val font40    = screen.fontGenerator_Ubuntu_Regular.generateFont(parameter.setBorder(1f, GameColor.purple_55).setSize(40))

    private val textFieldStyle = TextField.TextFieldStyle().apply {
            font       = font40
            fontColor  = Color.WHITE
            cursor     = TextureRegionDrawable(screen.drawerUtil.getTexture(Color.WHITE))
            selection  = TextureRegionDrawable(screen.drawerUtil.getTexture(Color.BLACK.cpy().apply { a = 0.25f }))
        }

    val textArea = TextArea("", textFieldStyle)

    var blockTextFieldListener: (String) -> Unit = {}

    override fun addActorsOnGroup() {
        addTextField()
    }

    override fun sizeChanged() {
        super.sizeChanged()
        textArea.setSize(width, height)
    }

    private fun addTextField() {
        addAndFillActor(textArea)

        textArea.alignment = Align.center

        textArea.setOnscreenKeyboard { visible ->
            Gdx.input.setOnscreenKeyboardVisible(visible, OnscreenKeyboardType.Default)
        }

        textArea.setTextFieldListener { _, key ->
            blockTextFieldListener(textArea.text)

           // if (key == '\n' || key == '\r') { // Перевіряємо Enter або Return
           //     stage.keyboardFocus = null
           //     Gdx.input.setOnscreenKeyboardVisible(false)
           // }
        }
    }

}