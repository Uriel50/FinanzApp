package com.camu.finanzapp.database

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update


import com.camu.finanzapp.util.Constants

interface BudgetDao {

    @Insert
    suspend fun  insertBudget(budget: BudgetEntity)

    //Read
    @Query("SELECT * FROM ${Constants.DATABASE_NAME_TABLE_BUDGET}")
    suspend fun getAllBudgets(): List<BudgetEntity>

    @Query("SELECT name_budget FROM ${Constants.DATABASE_NAME_TABLE_BUDGET} WHERE user_id = :user_id_budget LIMIT 1")
    suspend fun getNameBudgetByEmail(user_id_budget: String): Long?

    //Update
    @Update
    suspend fun updateBudget(user: BudgetEntity)

    //Delete
    @Delete
    suspend fun deleteBudget(user: BudgetEntity)
}