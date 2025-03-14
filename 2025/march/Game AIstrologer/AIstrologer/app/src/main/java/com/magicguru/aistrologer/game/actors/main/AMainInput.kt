package com.magicguru.aistrologer.game.actors.main

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.magicguru.aistrologer.game.actors.AGoroscop
import com.magicguru.aistrologer.game.actors.AInputPanel
import com.magicguru.aistrologer.game.actors.ALoader
import com.magicguru.aistrologer.game.actors.ATop
import com.magicguru.aistrologer.game.actors.button.AButton
import com.magicguru.aistrologer.game.actors.button.ATextButton
import com.magicguru.aistrologer.game.screens.GameScreen
import com.magicguru.aistrologer.game.screens.InputScreen
import com.magicguru.aistrologer.game.utils.Acts
import com.magicguru.aistrologer.game.utils.HEIGHT_UI
import com.magicguru.aistrologer.game.utils.TIME_ANIM_SCREEN
import com.magicguru.aistrologer.game.utils.WIDTH_UI
import com.magicguru.aistrologer.game.utils.actor.animDelay
import com.magicguru.aistrologer.game.utils.actor.animHide
import com.magicguru.aistrologer.game.utils.actor.animShow
import com.magicguru.aistrologer.game.utils.actor.disable
import com.magicguru.aistrologer.game.utils.actor.enable
import com.magicguru.aistrologer.game.utils.actor.setOnClickListener
import com.magicguru.aistrologer.game.utils.advanced.AdvancedMainGroup
import com.magicguru.aistrologer.game.utils.font.FontParameter
import com.magicguru.aistrologer.game.utils.gdxGame
import com.magicguru.aistrologer.util.log

class AMainInput(
    override val screen: InputScreen,
): AdvancedMainGroup() {

    private val parameter = FontParameter().setCharacters(FontParameter.CharType.ALL)
    private val font58    = screen.fontGenerator_Ubuntu_Regular.generateFont(parameter.setSize(58))

    private val ls58 = LabelStyle(font58, Color.WHITE)

    private val aTop        = ATop(screen)
    private val aLoader     = ALoader(screen)
    private val btnNext     = ATextButton(screen, "Далее", ls58, AButton.Type.Gradient)
    private val aInputPanel = AInputPanel(screen)
    private val aGoroscop   = AGoroscop(screen)
    private val aClickArea  = Actor()

    private var currentInputText = ""

    override fun addActorsOnGroup() {
        color.a = 0f
        addALoader()
        addTop()
        addInputPanel()
        addBtnNext()
        addGoroscop()

        animShowMain()

        keyboardListener()
    }

    // Actors ------------------------------------------------------------------------

    private fun addTop() {
        addActor(aTop)
        aTop.setBounds(44f, 1713f, 936f, 100f)

        aTop.blockBack = {
            InputScreen.CURRENT_INDEX--
            when {
                InputScreen.CURRENT_INDEX < 0 -> {
                    screen.stageUI.hideKeyboard()
                    screen.hideScreen { gdxGame.navigationManager.exit() }
                }
                InputScreen.CURRENT_INDEX == 1 -> {
                    aTop.update()
                    aInputPanel.update()

                    aInputPanel.enable()
                    hideGoroscop()
                }
                else -> {
                    aTop.update()
                    aInputPanel.update()
                }
            }
        }
    }

    private fun addInputPanel() {
        addActor(aInputPanel)
        aInputPanel.setBounds(44f, 723f, 936f, 337f)

        aInputPanel.blockInputText = { text ->
            currentInputText = text

            if (text.isBlank()) {
                btnNext.disable()
            } else {
                btnNext.enable()
            }
        }
    }

    private fun addALoader() {
        addActor(aLoader)
        aLoader.setBounds(387f, 1235f, 250f, 250f)
    }

    private fun addBtnNext() {
        addActor(btnNext)
        btnNext.setBounds(262f, 215f, 500f, 150f)
        btnNext.disable()

        btnNext.setOnClickListener {
            gdxGame.mapUser.put(InputScreen.CURRENT_INDEX, currentInputText.take(15))

            InputScreen.CURRENT_INDEX++

            when {
                InputScreen.CURRENT_INDEX > 2 -> {
                    gdxGame.ds_UserData.update {
                        it.apply {
                            uName        = gdxGame.mapUser[0]
                            placeOfBirth = gdxGame.mapUser[1]
                            dateOfBirth  = gdxGame.mapUser[2]
                        }
                    }
                    screen.stageUI.hideKeyboard()
                    screen.hideScreen { gdxGame.navigationManager.navigate(GameScreen::class.java.name, InputScreen::class.java.name) }
                }
                InputScreen.CURRENT_INDEX == 2 -> {
                    screen.stageUI.hideKeyboard()
                    aTop.update()
                    aInputPanel.update()

                    aInputPanel.disable()
                    showGoroscop()
                }
                else -> {
                    aTop.update()
                    aInputPanel.update(true)
                }
            }
        }
    }

    private fun addGoroscop() {
        addActor(aGoroscop)
        aGoroscop.color.a = 0f
        aGoroscop.setBounds(WIDTH_UI, HEIGHT_UI, 996f, 487f)

        addActor(aClickArea)
        aClickArea.setBounds(WIDTH_UI, HEIGHT_UI, 893f, 112f)
        aClickArea.setOnClickListener(gdxGame.soundUtil) {
            gdxGame.activity.showBirthDatePicker { date ->
                aInputPanel.setDate(date)
                btnNext.enable()
                currentInputText = date

                aGoroscop.update(date)
            }
        }
    }

    // Anim ------------------------------------------------

    override fun animShowMain(blockEnd: Runnable) {
        this.animShow(TIME_ANIM_SCREEN)
        this.animDelay(TIME_ANIM_SCREEN) { blockEnd.run() }
    }

    override fun animHideMain(blockEnd: Runnable) {
        this.animHide(TIME_ANIM_SCREEN)
        this.animDelay(TIME_ANIM_SCREEN) { blockEnd.run() }
    }

    // Logic -------------------------------------------------

    private fun animShowKeyboard() {
        var time = 0.2f

        aLoader.addAction(Acts.parallel(
            Acts.scaleTo(0.48f, 0.48f, time),
            Acts.moveTo(452f, 1511f, time),
        ))
        aInputPanel.addAction(Acts.moveTo(44f, 1094f, time))
        btnNext.addAction(Acts.moveTo(262f, 863f, time))
    }

    private fun animHideKeyboard() {
        var time = 0.2f

        aLoader.addAction(Acts.parallel(
            Acts.scaleTo(1f, 1f, time),
            Acts.moveTo(387f, 1235f, time),
        ))
        aInputPanel.addAction(Acts.moveTo(44f, 723f, time))
        btnNext.addAction(Acts.moveTo(262f, 215f, time))
    }

    private fun keyboardListener() {
        gdxGame.activity.blockKeyboardHeight.add { heightKeyboard ->
            if (heightKeyboard > 0) {
                animShowKeyboard()
            } else {
                screen.stageUI.keyboardFocus = null
                animHideKeyboard()
            }
        }
    }

    private fun showGoroscop() {
        aGoroscop.addAction(Acts.parallel(
            Acts.moveTo(14f, 1143f),
            Acts.fadeIn(0.35f)
        ))
        aLoader.addAction(Acts.sequence(
            Acts.fadeOut(0.35f),
            Acts.moveTo(WIDTH_UI, HEIGHT_UI)
        ))
        aClickArea.addAction(Acts.moveTo(66f, 757f))
    }

    private fun hideGoroscop() {
        aGoroscop.addAction(Acts.sequence(
            Acts.fadeOut(0.35f),
            Acts.moveTo(WIDTH_UI, HEIGHT_UI)
        ))
        aLoader.addAction(Acts.parallel(
            Acts.moveTo(387f, 1235f),
            Acts.fadeIn(0.35f)
        ))
        aClickArea.addAction(Acts.moveTo(WIDTH_UI, HEIGHT_UI))
    }

}