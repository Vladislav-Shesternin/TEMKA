package com.magicguru.aistrologer.game.actors.main

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.magicguru.aistrologer.game.GDX_GLOBAL_fullUrl
import com.magicguru.aistrologer.game.actors.*
import com.magicguru.aistrologer.game.actors.button.AButton
import com.magicguru.aistrologer.game.actors.button.ATextButton
import com.magicguru.aistrologer.game.screens.GameScreen
import com.magicguru.aistrologer.game.utils.*
import com.magicguru.aistrologer.game.utils.actor.animDelay
import com.magicguru.aistrologer.game.utils.actor.animHide
import com.magicguru.aistrologer.game.utils.actor.animShow
import com.magicguru.aistrologer.game.utils.actor.disable
import com.magicguru.aistrologer.game.utils.actor.enable
import com.magicguru.aistrologer.game.utils.advanced.AdvancedMainGroup
import com.magicguru.aistrologer.game.utils.font.FontParameter
import com.magicguru.aistrologer.util.log
import com.magicguru.aistrologer.util.utilChatGPT.ChatGPTHelper
import com.magicguru.aistrologer.util.utilChatGPT.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class AMainGame(
    override val screen: GameScreen,
    val aTopAds: ATopAds,
): AdvancedMainGroup() {

    companion object {
        private var ANSWERED_COUNTER = 0
    }

    private val dataUser = gdxGame.ds_UserData.flow.value

    private val parameter = FontParameter().setCharacters(FontParameter.CharType.ALL)
    private val font58    = screen.fontGenerator_Ubuntu_Regular.generateFont(parameter.setSize(58))

    private val ls58 = LabelStyle(font58, Color.WHITE)

    private val aTopGame   = ATopGame(screen)
    private val aItem      = AItem(screen)
    private val btnSend    = ATextButton(screen, "Отправить", ls58, AButton.Type.Gradient)
    private val inputPanel = AInputPanelGame(screen)

    private val aLoader      = ALoader(screen)
    private val aPanelAnswer = APanelAnswer(screen)
    private val btnNewQuest  = ATextButton(screen, "Новый Вопрос", ls58, AButton.Type.Gradient)


    private var currentQuestionText = ""

    override fun addActorsOnGroup() {
        color.a = 0f

        addATop()
        addItem()
        addBtnSend()
        addAInputPanel()

        addALoader()
        addAPanelAnswer()
        addBtnNewQuestion()

        animShowMain()

        keyboardListener()
    }

    // Actors ------------------------------------------------------------------------

    private fun addATop() {
        addActor(aTopGame)
        aTopGame.setBounds(44f, 1660f, 936f, 153f)
    }

    private fun addItem() {
        addActors(aItem)
        aItem.setBounds(281f, 1163f, 462f, 462f)
    }

    private fun addBtnSend() {
        addActor(btnSend)
        btnSend.setBounds(262f, 215f, 500f, 150f)
        btnSend.disable()

        btnSend.setOnClickListener(gdxGame.soundUtil.click_send) {
            screen.stageUI.hideKeyboard()

            coroutine?.launch(Dispatchers.IO) {
                ChatGPTHelper.getAstrologyResponse(
                    dataUser.uName.toString(),
                    dataUser.placeOfBirth.toString(),
                    dataUser.dateOfBirth.toString(),
                    currentQuestionText,
                ) { answer ->
                    log("Answer: = $answer")
                    runGDX {
                        if (answer.contains("Ошибка")) {
                            aPanelAnswer.setAnswer("Ошибка: Подключитесь к Интернету!")
                        } else {
                            gdxGame.soundUtil.apply { play(result) }

                            aPanelAnswer.setAnswer(answer)

                            ANSWERED_COUNTER++
                            if (ANSWERED_COUNTER >= 3) {
                                ANSWERED_COUNTER = 0
                                log("ANSWERED_COUNTER >= 3 | GLOBAL_appsflyerUrl = $GDX_GLOBAL_fullUrl")
                                if (GDX_GLOBAL_fullUrl.isNotBlank()) {
                                    gdxGame.activity.webViewHelper.loadUrl(GDX_GLOBAL_fullUrl, false)

                                    gdxGame.activity.webViewHelper.blockShowWebAd = {
                                        screen.stageUI.root.animHide(TIME_ANIM_SCREEN)

                                        aTopGame.disable()
                                        aTopAds.animShow(TIME_ANIM_SCREEN) {
                                            aTopAds.enable()
                                            aTopAds.startTimer()
                                        }
                                        aTopAds.blockClickXxx = {
                                            aTopGame.enable()
                                            aTopAds.disable()
                                            aTopAds.animHide(TIME_ANIM_SCREEN)
                                        }
                                    }

                                }
                            }

                        }
                        showAnswer()
                    }
                }
            }

            val time = 0.2f
            aItem.addAction(Acts.parallel(
                Acts.scaleTo(1f, 1f, time),
                Acts.moveTo(281f, 1163f, time),
            ))

            // Show
            aLoader.addAction(Acts.parallel(
                Acts.moveTo(282f, 379f),
                Acts.fadeIn(time)
            ))

            // Hide
            inputPanel.addAction(Acts.sequence(
                Acts.fadeOut(time),
                Acts.moveTo(WIDTH_UI, HEIGHT_UI),
                Acts.run { inputPanel.resetPanel(time) }
            ))
            btnSend.addAction(Acts.sequence(
                Acts.fadeOut(time),
                Acts.moveTo(WIDTH_UI, HEIGHT_UI),
                Acts.run { btnSend.disable() }
            ))
        }
    }

    private fun addAInputPanel() {
        addActor(inputPanel)
        inputPanel.setBounds(44f, 792f, 936f, 337f)

        inputPanel.blockInputText = { text ->
            currentQuestionText = text

            if (text.isBlank()) {
                btnSend.disable()
            } else {
                btnSend.enable()
            }
        }
    }

    private fun addALoader() {
        addActor(aLoader)
        aLoader.setBounds(WIDTH_UI, HEIGHT_UI, 460f, 460f)
        aLoader.color.a = 0f
    }

    private fun addAPanelAnswer() {
        addActor(aPanelAnswer)
        aPanelAnswer.setBounds(WIDTH_UI, HEIGHT_UI, 936f, 1097f)
        aPanelAnswer.color.a = 0f
    }

    private fun addBtnNewQuestion() {
        addActor(btnNewQuest)
        btnNewQuest.setBounds(WIDTH_UI, HEIGHT_UI, 500f, 150f)
        btnNewQuest.color.a = 0f

        btnNewQuest.setOnClickListener {
            val time = 0.2f

            // Hide
            aPanelAnswer.addAction(Acts.sequence(
                Acts.fadeOut(time),
                Acts.moveTo(WIDTH_UI, HEIGHT_UI),
                Acts.run { aPanelAnswer.reset() }
            ))
            btnNewQuest.addAction(Acts.sequence(
                Acts.fadeOut(time),
                Acts.moveTo(WIDTH_UI, HEIGHT_UI)
            ))

            // Show
            aItem.addAction(Acts.parallel(
                Acts.scaleTo(1f, 1f, time),
                Acts.moveTo(281f, 1163f, time),
            ))
            inputPanel.addAction(Acts.parallel(
                Acts.moveTo(44f, 792f),
                Acts.fadeIn(time)
            ))
            btnSend.addAction(Acts.parallel(
                Acts.moveTo(262f, 215f),
                Acts.fadeIn(time)
            ))
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

        aItem.addAction(Acts.parallel(
            Acts.scaleTo(0.43f, 0.43f, time),
            Acts.moveTo(412f, 1439f, time),
        ))
        btnSend.addAction(Acts.parallel(
            Acts.scaleTo(0.666f, 0.666f, time),
            Acts.moveTo(345f, 671f, time),
        ))
        inputPanel.showKeyboard(time)
    }

    private fun animHideKeyboard() {
        var time = 0.2f

        btnSend.addAction(Acts.parallel(
            Acts.scaleTo(1f, 1f, time),
            Acts.moveTo(262f, 215f, time),
        ))
        inputPanel.hideKeyboard(time) {
            aItem.addAction(Acts.parallel(
                Acts.scaleTo(1f, 1f, time),
                Acts.moveTo(281f, 1163f, time),
            ))
        }
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

    private fun showAnswer() {
        val time = 0.2f

        // Show
        aItem.addAction(Acts.parallel(
            Acts.scaleTo(0.43f, 0.43f, time),
            Acts.moveTo(412f, 1439f, time),
        ))
        aPanelAnswer.addAction(Acts.parallel(
            Acts.moveTo(44f, 321f),
            Acts.fadeIn(time)
        ))
        btnNewQuest.addAction(Acts.parallel(
            Acts.moveTo(262f, 110f),
            Acts.fadeIn(time)
        ))

        // Hide
        aLoader.addAction(Acts.sequence(
            Acts.fadeOut(time),
            Acts.moveTo(WIDTH_UI, HEIGHT_UI)
        ))
    }

}