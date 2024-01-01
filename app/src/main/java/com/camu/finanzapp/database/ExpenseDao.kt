package com.camu.finanzapp.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.camu.finanzapp.util.Constants
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

    @Query("SELECT SUM(expense_mount) FROM ${Constants.DATABASE_NAME_TABLE_EXPENSE} WHERE user_email_expense = :email")
    fun getTotalExpenseByEmail(email: String): Double

    @Query("SELECT * FROM ${DATABASE_NAME_TABLE_EXPENSE} WHERE user_email_expense = :email")
    fun getAllExpenseLiveData(email: String): LiveData<List<ExpenseEntity>>

    @Query("SELECT SUM(expense_mount) FROM ${DATABASE_NAME_TABLE_EXPENSE} WHERE user_email_expense = :email AND expense_category = :category")
    suspend fun getExpensesByCategory(email: String, category: String): Double

    @Query("SELECT SUM(expense_mount) FROM ${DATABASE_NAME_TABLE_EXPENSE} WHERE user_email_expense = :email AND SUBSTR(expense_date, 0, INSTR(expense_date, '/')) = :month")
    suspend fun getExpensesByMonth(email: String, month: String): Double

}
