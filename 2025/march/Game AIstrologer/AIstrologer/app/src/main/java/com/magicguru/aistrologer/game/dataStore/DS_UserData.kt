package com.magicguru.aistrologer.game.dataStore

import com.magicguru.aistrologer.game.manager.DataStoreManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer

class DS_UserData(override val coroutine: CoroutineScope): DataStoreJsonUtil<DataUser>(
    serializer   = DataUser.serializer(),
    deserializer = DataUser.serializer(),
) {

    override val dataStore = DataStoreManager.DataUser

    override val flow = MutableStateFlow(DataUser())

    init { initialize() }

}

@Serializable
data class DataUser(
    var uName       : String? = null,
    var placeOfBirth: String? = null,
    var dateOfBirth : String? = null,
)