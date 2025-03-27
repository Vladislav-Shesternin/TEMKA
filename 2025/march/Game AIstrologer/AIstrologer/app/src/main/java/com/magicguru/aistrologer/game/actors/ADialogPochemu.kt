package com.magicguru.aistrologer.game.actors

import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane
import com.badlogic.gdx.utils.Align
import com.magicguru.aistrologer.game.utils.GameColor
import com.magicguru.aistrologer.game.utils.advanced.AdvancedGroup
import com.magicguru.aistrologer.game.utils.advanced.AdvancedScreen
import com.magicguru.aistrologer.game.utils.font.FontParameter
import com.magicguru.aistrologer.game.utils.gdxGame

class ADialogPochemu(override val screen: AdvancedScreen) : AdvancedGroup() {

    private val parameter     = FontParameter().setCharacters(FontParameter.CharType.ALL)
    private val font35        = screen.fontGenerator_Ubuntu_Regular.generateFont(parameter.setSize(35))
    private val font35_Stroke = screen.fontGenerator_Ubuntu_Regular.generateFont(parameter.setBorder(1f, GameColor.background).setSize(40))

    private val ls35        = LabelStyle(font35, GameColor.background)
    private val ls35_Stroke = LabelStyle(font35_Stroke, GameColor.background)

    private val textTitle = "Почему я это вижу"
    private val textDesc  = """Это приложение распространяется по модели бесплатного доступа и, в соответствии с пунктом N условий использования (Terms and Conditions), может содержать внешнюю контекстную рекламу.

Пожалуйста, обратите внимание:

Содержание рекламных материалов не контролируется авторами приложения.

Все рекламные объявления предоставляются сторонними сервисами и могут вести на внешние ресурсы.

Переход по рекламе и любые последующие действия совершаются пользователем добровольно и на собственную ответственность.

Если у вас возникли вопросы или сомнения по поводу конкретной рекламы, рекомендуем воздержаться от взаимодействия с ней."""

    private val imgPochemu  = Image(gdxGame.assetsAll.POCHEMU)
    private val lblTitle    = Label(textTitle, ls35_Stroke)
    private val lblDesc     = Label(textDesc, ls35)
    private val scroll      = ScrollPane(lblDesc)

    override fun addActorsOnGroup() {
        addAndFillActor(imgPochemu)
        addLblTitle()
        addScroll()
    }

    // Actors ------------------------------------------------------------------------

    private fun addLblTitle() {
        addActor(lblTitle)
        lblTitle.setBounds(84f, 361f, 812f, 51f)
        lblTitle.setAlignment(Align.center)
    }

    private fun addScroll() {
        addActor(scroll)
        scroll.setBounds(23f, 13f, 936f, 337f)

        lblDesc.setSize(936f, 337f)
        lblDesc.setAlignment(Align.center)
        lblDesc.wrap = true
    }

}