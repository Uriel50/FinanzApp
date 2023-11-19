package com.camu.finanzapp.database

import androidx.room.*
import com.camu.finanzapp.util.Constants.DATABASE_NAME_TABLE_TOTAL


@Dao
interface TotalDao {

    @Insert
    suspend fun insertTotal(total: TotalsEntity)

    @Query("SELECT * FROM ${DATABASE_NAME_TABLE_TOTAL}")
    suspend fun getAllTotals(): List<TotalsEntity>

    @Update
    suspend fun updateTotals(total: TotalsEntity)

    @Delete
    suspend fun deleteTotals(total: TotalsEntity)
}
