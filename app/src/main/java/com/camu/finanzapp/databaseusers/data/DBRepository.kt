package com.camu.finanzapp.databaseusers.data

import com.camu.finanzapp.databaseusers.data.db.DBDao
import com.camu.finanzapp.databaseusers.data.db.model.DBEntity

// Clase DBRepository que actúa como intermediario entre la base de datos y la capa de la aplicación
class DBRepository(private val userDao: DBDao) {

    // Función suspendida para insertar un usuario en la base de datos
    suspend fun insertUser(user: DBEntity) {
        userDao.insertUser(user)
    }

    // Función suspendida para obtener la lista de usuarios desde la base de datos
    suspend fun getUser(): List<DBEntity> = userDao.getUser()

    suspend fun getUserByEmail(email: String): DBEntity? {
        return userDao.getUserByEmail(email)
    }

    // Función suspendida para actualizar un usuario en la base de datos
    suspend fun updateUser(user: DBEntity) {
        userDao.updateUser(user)
    }

    // Función suspendida para eliminar un usuario de la base de datos
    suspend fun deleteUser(user: DBEntity) {
        userDao.deleteUser(user)
    }
}
