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