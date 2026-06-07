package com.example.mistercalculator.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface HistoryDao {
    @Insert
    suspend fun insertHistory(
        history: HistoryEntity
    )

    @Query("SELECT * FROM history ORDER BY id DESC")
    fun getAllHistory():
            kotlinx.coroutines.flow.Flow<List<HistoryEntity>>

    @Query("DELETE FROM history")
    suspend fun clearHistory()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveCalculatorState(
        state: CalculatorStateEntity
    )

    @Query("SELECT * FROM calculator_state WHERE id = 1")
    suspend fun getCalculatorState():
            CalculatorStateEntity?
}