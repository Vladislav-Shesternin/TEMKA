package com.magicguru.aistrologer.util

import android.graphics.Rect
import android.view.View
import com.badlogic.gdx.Gdx

class KeyboardHeightListener(
    private val rootView: View,
    private val callback: (Int) -> Unit) {

    private var previousHeight = 0

    fun startListening() {
        rootView.viewTreeObserver.addOnGlobalLayoutListener {
            val rect = Rect()
            rootView.getWindowVisibleDisplayFrame(rect)

            val screenHeight  = rootView.height
            val visibleHeight = rect.height()

            // Якщо є різниця між екраном та видимою областю — клавіатура відкрита
            val keyboardHeight = screenHeight - visibleHeight

            if (keyboardHeight != previousHeight) {
                callback(keyboardHeight)
                previousHeight = keyboardHeight
            }
        }
    }
}



/*
class KeyboardHeightListener(
    private val rootView: View,
    private val callback: (isKeyboardVisible: Boolean, keyboardHeight: Int) -> Unit
) {

    private var previousHeight = 0
    private var baseInset = -1 // висота, коли клавіатура закрита

    fun startListening() {
        rootView.viewTreeObserver.addOnGlobalLayoutListener {
            val rect = Rect()
            rootView.getWindowVisibleDisplayFrame(rect)

            val screenHeight = rootView.height
            val visibleHeight = rect.height()

            val keyboardHeight = screenHeight - visibleHeight

            if (baseInset == -1) {
                baseInset = keyboardHeight // запам’ятовуємо як "висоту без клавіатури"
                log("KeyboardHeight BaseInset set to $baseInset")
            }

            val isVisible = keyboardHeight > baseInset + 20 // буфер для помилок

            if (keyboardHeight != previousHeight) {
                previousHeight = keyboardHeight
                callback(isVisible, keyboardHeight)
            }
        }
    }
}*/
