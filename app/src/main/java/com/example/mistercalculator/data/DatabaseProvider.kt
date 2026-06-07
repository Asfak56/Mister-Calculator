package com.example.mistercalculator.data

import android.content.Context
import androidx.room.Room

object DatabaseProvider {
    @Volatile
    private var INSTANCE: CalculatorDatabase? = null

    fun getDatabase(
        context: Context
    ): CalculatorDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                CalculatorDatabase::class.java,
                "calculator_db"
            )
                .fallbackToDestructiveMigration()
                .build()
            INSTANCE = instance
            instance
        }
    }
}