package com.magicguru.aistrologer.game.actors

import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.utils.Align
import com.magicguru.aistrologer.game.actors.button.AButton
import com.magicguru.aistrologer.game.actors.label.ALabel
import com.magicguru.aistrologer.game.actors.label.ALabelSpinning
import com.magicguru.aistrologer.game.utils.Acts
import com.magicguru.aistrologer.game.utils.GameColor
import com.magicguru.aistrologer.game.utils.TIME_ANIM_SCREEN
import com.magicguru.aistrologer.game.utils.actor.animHide
import com.magicguru.aistrologer.game.utils.actor.animShow
import com.magicguru.aistrologer.game.utils.actor.disable
import com.magicguru.aistrologer.game.utils.actor.enable
import com.magicguru.aistrologer.game.utils.advanced.AdvancedGroup
import com.magicguru.aistrologer.game.utils.advanced.AdvancedScreen
import com.magicguru.aistrologer.game.utils.font.FontParameter
import com.magicguru.aistrologer.game.utils.gdxGame
import com.magicguru.aistrologer.game.utils.runGDX
import com.magicguru.aistrologer.util.log
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class ATopAds(override val screen: AdvancedScreen) : AdvancedGroup() {
    
    private val parameter = FontParameter().setCharacters(FontParameter.CharType.ALL)
    private val font58    = screen.fontGenerator_Ubuntu_Regular.generateFont(parameter.setSize(58))
    private val font25    = screen.fontGenerator_Ubuntu_Regular.generateFont(parameter.setSize(25))

    private val ls58 = LabelStyle(font58, GameColor.background)
    private val ls25 = LabelStyle(font25, GameColor.background)

    var blockClickXxx = {}

    private var timer = 10

    private val textSpinning = "Благодаря этой рекламе Вы можете использовать \"Твой Астролог\" Бесплатно."

    private val imgTop       = Image(gdxGame.assetsAll.top)
    private val imgTimeFrame = Image(gdxGame.assetsAll.frame_time)
    private val btnPitanie   = AButton(screen, AButton.Type.Pitanie)
    private val btnXxx       = AButton(screen, AButton.Type.Xxx)
    private val lblAds       = Label("Реклама", ls58)
    private val lblTimer     = ALabel(screen, timer.toString(), ls58)
    private val imgSpinning  = Image(gdxGame.assetsAll.spinning)
    private val lblSpinning  = ALabelSpinning(screen, textSpinning, ls25, Align.left, 4f, 10f)
    private val aPochemu     = ADialogPochemu(screen)

    override fun addActorsOnGroup() {
        addAndFillActor(imgTop)
        addActor(imgTimeFrame)
        imgTimeFrame.setBounds(811f, 8f, 101f, 84f)

        addLblTitle()
        addLblTimer()
        addBtnPitanie()
        addBtnXxx()
        addSpinning()
        addImgPochemu()
    }

    // Actors ------------------------------------------------------------------------

    private fun addBtnPitanie() {
        addActor(btnPitanie)
        btnPitanie.apply {
            setBounds(24f, 8f, 101f, 84f)
            setOnClickListener {
                if (aPochemu.color.a < 1f) {
                    aPochemu.animShow(0.3f)
                    gdxGame.activity.webViewHelper.updateHeight(gdxGame.activity.webViewHelper.topY_Dialog)
                } else {
                    aPochemu.animHide(0.3f)
                    gdxGame.activity.webViewHelper.updateHeight(gdxGame.activity.webViewHelper.topY)
                }
            }
        }
    }

    private fun addBtnXxx() {
        addActor(btnXxx)
        btnXxx.apply {
            setBounds(811f, 8f, 101f, 84f)
            color.a = 0f
            disable()

            fun hideWebAd() {
                runGDX {
                    btnXxx.disable()
                    btnXxx.animHide(0.2f) {
                        lblTimer.label.setText("10")
                        lblTimer.animShow(0.2f)
                        aPochemu.animHide(0.2f)
                        gdxGame.activity.webViewHelper.updateHeight(gdxGame.activity.webViewHelper.topY)
                    }
                }

                gdxGame.activity.hideWebView()
                screen.stageUI.root.animShow(TIME_ANIM_SCREEN)
                blockClickXxx()
            }

            gdxGame.activity.webViewHelper.blockRedirectThankKey = { hideWebAd() }

            setOnClickListener { hideWebAd() }
        }
    }

    private fun addSpinning() {
        addActor(imgSpinning)
        imgSpinning.setBounds(0f, -59f, 936f, 43f)

        addActor(lblSpinning)
        lblSpinning.setBounds(12f, -55f, 911f, 36f)
    }

    private fun addLblTitle() {
        addActor(lblAds)
        lblAds.setBounds(0f, 8f, 936f, 84f)
        lblAds.setAlignment(Align.center)
    }

    private fun addLblTimer() {
        addActor(lblTimer)
        lblTimer.setBounds(834f, 8f, 55f, 84f)
        lblTimer.label.setAlignment(Align.center)
        lblTimer.setOrigin(Align.center)
    }

    private fun addImgPochemu() {
        addActor(aPochemu)
        aPochemu.setBounds(-23f, -507f, 981f, 432f)
        aPochemu.color.a = 0f
    }

    // Logic -----------------------------------------------------------------------

    fun startTimer() {
        timer = 10

        lblTimer.clearActions()
        lblTimer.addAction(Acts.forever(Acts.sequence(
            Acts.scaleTo(0.75f, 0.75f, 0.5f),
            Acts.scaleTo(1f, 1f, 0.5f),
        )))

        coroutine?.launch {
            while (timer > 1 && isActive) {
                delay(500)
                timer--

                runGDX {
                    lblTimer.label.setText(timer)
                }
                delay(500)
            }
            runGDX {
                lblTimer.clearActions()
                lblTimer.animHide(0.2f)
                btnXxx.animShow(0.2f) {
                    btnXxx.enable()
                }
            }
        }
    }

}