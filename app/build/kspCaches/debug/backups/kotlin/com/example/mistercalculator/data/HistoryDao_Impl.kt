package com.example.mistercalculator.`data`

import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import javax.`annotation`.processing.Generated
import kotlin.Int
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.mutableListOf
import kotlin.reflect.KClass
import kotlinx.coroutines.flow.Flow

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class HistoryDao_Impl(
  __db: RoomDatabase,
) : HistoryDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfHistoryEntity: EntityInsertAdapter<HistoryEntity>

  private val __insertAdapterOfCalculatorStateEntity: EntityInsertAdapter<CalculatorStateEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfHistoryEntity = object : EntityInsertAdapter<HistoryEntity>() {
      protected override fun createQuery(): String = "INSERT OR ABORT INTO `history` (`id`,`expression`,`result`) VALUES (nullif(?, 0),?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: HistoryEntity) {
        statement.bindLong(1, entity.id.toLong())
        statement.bindText(2, entity.expression)
        statement.bindText(3, entity.result)
      }
    }
    this.__insertAdapterOfCalculatorStateEntity = object : EntityInsertAdapter<CalculatorStateEntity>() {
      protected override fun createQuery(): String = "INSERT OR REPLACE INTO `calculator_state` (`id`,`expression`,`result`) VALUES (?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: CalculatorStateEntity) {
        statement.bindLong(1, entity.id.toLong())
        statement.bindText(2, entity.expression)
        statement.bindText(3, entity.result)
      }
    }
  }

  public override suspend fun insertHistory(history: HistoryEntity): Unit = performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfHistoryEntity.insert(_connection, history)
  }

  public override suspend fun saveCalculatorState(state: CalculatorStateEntity): Unit = performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfCalculatorStateEntity.insert(_connection, state)
  }

  public override fun getAllHistory(): Flow<List<HistoryEntity>> {
    val _sql: String = "SELECT * FROM history ORDER BY id DESC"
    return createFlow(__db, false, arrayOf("history")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfExpression: Int = getColumnIndexOrThrow(_stmt, "expression")
        val _columnIndexOfResult: Int = getColumnIndexOrThrow(_stmt, "result")
        val _result: MutableList<HistoryEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: HistoryEntity
          val _tmpId: Int
          _tmpId = _stmt.getLong(_columnIndexOfId).toInt()
          val _tmpExpression: String
          _tmpExpression = _stmt.getText(_columnIndexOfExpression)
          val _tmpResult: String
          _tmpResult = _stmt.getText(_columnIndexOfResult)
          _item = HistoryEntity(_tmpId,_tmpExpression,_tmpResult)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getCalculatorState(): CalculatorStateEntity? {
    val _sql: String = "SELECT * FROM calculator_state WHERE id = 1"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfExpression: Int = getColumnIndexOrThrow(_stmt, "expression")
        val _columnIndexOfResult: Int = getColumnIndexOrThrow(_stmt, "result")
        val _result: CalculatorStateEntity?
        if (_stmt.step()) {
          val _tmpId: Int
          _tmpId = _stmt.getLong(_columnIndexOfId).toInt()
          val _tmpExpression: String
          _tmpExpression = _stmt.getText(_columnIndexOfExpression)
          val _tmpResult: String
          _tmpResult = _stmt.getText(_columnIndexOfResult)
          _result = CalculatorStateEntity(_tmpId,_tmpExpression,_tmpResult)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun clearHistory() {
    val _sql: String = "DELETE FROM history"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
