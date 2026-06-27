package com.example.mistercalculator.data

import kotlinx.coroutines.flow.Flow

class HistoryRepository(
    private val historyDao: HistoryDao
) {
    suspend fun insertHistory(
        history: HistoryEntity
    ) {
        historyDao.insertHistory(history)
    }

    suspend fun getAllHistory():
            Flow<List<HistoryEntity>> {

        return historyDao.getAllHistory()
    }

    suspend fun clearHistory() {
        historyDao.clearHistory()
    }

    suspend fun saveCalculatorState(
        state: CalculatorStateEntity
    ) {
        historyDao.saveCalculatorState(state)
    }

    suspend fun getCalculatorState():
            CalculatorStateEntity? {

        return historyDao.getCalculatorState()
    }
}

