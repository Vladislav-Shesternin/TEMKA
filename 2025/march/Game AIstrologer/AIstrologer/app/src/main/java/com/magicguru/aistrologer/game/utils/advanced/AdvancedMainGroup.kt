package com.magicguru.aistrologer.game.utils.advanced

abstract class AdvancedMainGroup : AdvancedGroup() {

    // Anim ------------------------------------------------

    abstract fun animShowMain(blockEnd: Runnable = Runnable {  })

    abstract fun animHideMain(blockEnd: Runnable = Runnable {  })

}