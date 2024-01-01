package com.camu.finanzapp.database

import androidx.room.*
import com.camu.finanzapp.util.Constants
import com.camu.finanzapp.util.Constants.DATABASE_NAME_TABLE_TOTAL
import androidx.lifecycle.LiveData




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


    @Transaction
    @Query("SELECT * FROM ${DATABASE_NAME_TABLE_TOTAL} WHERE user_email_total = :email")
    suspend fun getTotalByEmail(email: String): TotalsEntity


    @Transaction
    @Query("UPDATE ${DATABASE_NAME_TABLE_TOTAL} SET total_income = :newTotalIncome WHERE user_email_total = :email")
    suspend fun updateTotalIncome(newTotalIncome: Double, email: String)


    @Transaction
    @Query("UPDATE ${DATABASE_NAME_TABLE_TOTAL} SET total_expense = :newTotalExpense WHERE user_email_total = :email")
    suspend fun updateTotalExpense(newTotalExpense: Double, email: String)


    @Transaction
    @Query("UPDATE ${DATABASE_NAME_TABLE_TOTAL} SET balance_total = :newBalanceTotal WHERE user_email_total = :email")
    suspend fun updateBalanceTotal(newBalanceTotal: Double, email: String)


    @Query("SELECT * FROM ${DATABASE_NAME_TABLE_TOTAL} WHERE user_email_total = :email")
    fun getTotalByEmailLiveData(email: String): LiveData<TotalsEntity>

}
