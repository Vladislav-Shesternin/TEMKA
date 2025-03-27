package com.magicguru.aistrologer.util

import android.util.Log
import com.magicguru.aistrologer.appContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel

fun log(message: String) {
    Log.i("Astronomy", message)
}

fun cancelCoroutinesAll(vararg coroutine: CoroutineScope?) {
    coroutine.forEach { it?.cancel() }
}

fun Int.toDp(): Int {
    return (this / appContext.resources.displayMetrics.density).toInt()
}

fun Int.toPx(): Int {
    return (this * appContext.resources.displayMetrics.density).toInt()
}