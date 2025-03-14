package com.magicguru.aistrologer.game.actors.main

import com.magicguru.aistrologer.game.actors.ALoader
import com.magicguru.aistrologer.game.screens.LoaderScreen
import com.magicguru.aistrologer.game.utils.advanced.AdvancedGroup

class AMainLoader(
    override val screen: LoaderScreen,
): AdvancedGroup() {

    val aLoader = ALoader(screen)

    override fun addActorsOnGroup() {
        addALoader()

    }

    // Actors ------------------------------------------------------------------------

    private fun addALoader() {
        addActor(aLoader)
        aLoader.setBounds(378f, 798f, 323f, 323f)
    }

}