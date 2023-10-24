package com.camu.finanzapp.database.application

import android.app.Application
import com.camu.finanzapp.database.data.DBRepository
import com.camu.finanzapp.database.data.db.DBDataBase

// Clase que representa la aplicación FinanzAppDB, que hereda de la clase Application
class FinanzAppDB : Application() {

    // Base de datos de la aplicación, inicializada de forma diferida (lazy)
    private val database by lazy {
        DBDataBase.getDataBase(this@FinanzAppDB)
    }

    // Repositorio de la base de datos, inicializado de forma diferida (lazy)
    val repository by lazy {
        // Se crea un repositorio utilizando el userDao de la base de datos
        DBRepository(database.userDao())
    }
}
