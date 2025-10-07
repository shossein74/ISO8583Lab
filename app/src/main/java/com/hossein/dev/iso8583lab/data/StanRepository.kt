package com.hossein.dev.iso8583lab.data

import kotlinx.coroutines.flow.Flow

class StanRepository(
    private val dataStore: StanDataStore
) {
    val stanFlow: Flow<Int> get() = dataStore.stanFlow
    suspend fun saveStan(value: Int) = dataStore.saveStan(value)
}