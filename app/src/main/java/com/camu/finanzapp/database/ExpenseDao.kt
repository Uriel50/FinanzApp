package com.camu.finanzapp.database

import androidx.room.*
import com.camu.finanzapp.util.Constants.DATABASE_NAME_TABLE_EXPENSE


@Dao
interface ExpenseDao {

    @Insert
    suspend fun insertExpense(expense: ExpenseEntity)

    @Query("SELECT * FROM ${DATABASE_NAME_TABLE_EXPENSE}")
    suspend fun getAllExpenses(): List<ExpenseEntity>

    @Update
    suspend fun updateExpense(expense: ExpenseEntity)

    @Delete
    suspend fun deleteExpense(expense: ExpenseEntity)
}
