package com.magicguru.aistrologer.game.dataStore

import com.magicguru.aistrologer.game.manager.AbstractDataStore
import com.magicguru.aistrologer.util.log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

abstract class DataStoreUtil<T> {
    val simpleName: String get() = this::class.java.simpleName

    abstract val coroutine: CoroutineScope
    abstract val flow     : MutableStateFlow<T>
    abstract val dataStore: AbstractDataStore.DataStoreElement<T>

    open fun initialize() {
        coroutine.launch(Dispatchers.IO) {
            dataStore.get()?.let { value -> flow.value = value }
            log("Store $simpleName = ${flow.value}")
        }
    }

    open fun update(block: (T) -> T) {
        coroutine.launch(Dispatchers.IO) {
            flow.value = block(flow.value)

            log("Store $simpleName update = ${flow.value}")
            dataStore.update { flow.value }
        }
    }
}
