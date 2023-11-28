package com.camu.finanzapp.database

import androidx.room.*
import com.camu.finanzapp.util.Constants
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

    @Query("SELECT * FROM ${DATABASE_NAME_TABLE_TOTAL} WHERE id_total = :idTotal")
    suspend fun getTotalById(idTotal: Long): TotalsEntity?

    @Query("SELECT * FROM ${DATABASE_NAME_TABLE_TOTAL} WHERE user_email_total = :email")
    suspend fun getTotalByEmail(email: String): TotalsEntity

    @Query("UPDATE ${DATABASE_NAME_TABLE_TOTAL} SET total_income = :newTotalIncome WHERE user_email_total = :email")
    suspend fun updateTotalIncome(newTotalIncome: Double, email: String)

    @Query("UPDATE ${DATABASE_NAME_TABLE_TOTAL} SET total_expense = :newTotalExpense WHERE user_email_total = :email")
    suspend fun updateTotalExpense(newTotalExpense: Double, email: String)

    @Query("UPDATE ${DATABASE_NAME_TABLE_TOTAL} SET balance_total = :newBalanceTotal WHERE user_email_total = :email")
    suspend fun updateBalanceTotal(newBalanceTotal: Double, email: String)

}
