package com.camu.finanzapp.database

import androidx.room.*
import com.camu.finanzapp.util.Constants.DATABASE_NAME_TABLE_INCOME


@Dao
interface IncomeDao {

    @Insert
    suspend fun insertIncome(income: IncomeEntity)

    @Query("SELECT * FROM ${DATABASE_NAME_TABLE_INCOME}")
    suspend fun getAllIncome(): List<IncomeEntity>

    @Update
    suspend fun updateIncome(income: IncomeEntity)

    @Delete
    suspend fun deleteIncome(income: IncomeEntity)
}
