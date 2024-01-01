package com.camu.finanzapp.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.camu.finanzapp.util.Constants.DATABASE_NAME_TABLE_INCOME


@Dao
interface IncomeDao {

    @Insert
    suspend fun insertIncome(income: IncomeEntity)

    @Query("SELECT * FROM ${DATABASE_NAME_TABLE_INCOME} WHERE user_email_income = :email")
    fun getAllIncomeLiveData(email: String): LiveData<List<IncomeEntity>>

    @Query("SELECT * FROM ${DATABASE_NAME_TABLE_INCOME}")
    suspend fun getAllIncome(): List<IncomeEntity>

    @Update
    suspend fun updateIncome(income: IncomeEntity)

    @Delete
    suspend fun deleteIncome(income: IncomeEntity)

    @Query("SELECT SUM(income_mount) FROM ${DATABASE_NAME_TABLE_INCOME} WHERE user_email_income = :email")
    suspend fun getTotalIncomeByEmail(email: String): Double

    @Query("SELECT SUM(income_mount) FROM ${DATABASE_NAME_TABLE_INCOME} WHERE user_email_income = :email AND income_category = :category")
    suspend fun getIncomesByCategory(email: String, category: String): Double

    @Query("SELECT SUM(income_mount) FROM ${DATABASE_NAME_TABLE_INCOME} WHERE user_email_income = :email AND SUBSTR(income_date, 0, INSTR(income_date, '/')) = :month")
    suspend fun getIncomesByMonth(email: String, month: String): Double



}
