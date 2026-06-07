package com.example.mistercalculator.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "calculator_state")
data class CalculatorStateEntity(
    @PrimaryKey
    val id: Int = 1,
    val expression: String,
    val result: String
)