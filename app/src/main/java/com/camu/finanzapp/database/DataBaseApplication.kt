package com.camu.finanzapp.database

import android.app.Application

class DataBaseApplication (): Application(){

    // Base de datos de la aplicación, inicializada de forma diferida (lazy)
    private val database by lazy {
        DataBase.getDataBase(this@DataBaseApplication)
    }

    // Repositorio de la base de datos, inicializado de forma diferida (lazy)
    val repository by lazy {
        DataBaseRepository(
            userDao = database.userDao(),
            totalDao = database.totalDao(),
            reminderDao = database.reminderDao(),
            incomeDao = database.incomeDao(),
            expenseDao = database.expenseDao(),
            budgetDao = database.budgetDao()
        )
    }

}