package com.camu.finanzapp.database.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.camu.finanzapp.database.data.db.model.DBEntity
import com.camu.finanzapp.util.Constants.DATABASE_NAME_TABLE


@Dao
interface DBDao {

    //Create
    @Insert
    suspend fun  insertUser(user: DBEntity)

    //Read
    @Query("SELECT * FROM ${DATABASE_NAME_TABLE}")
    suspend fun getUser(): List<DBEntity>

    @Query("SELECT * FROM ${DATABASE_NAME_TABLE} WHERE user_email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): DBEntity?

    //Update
    @Update
    suspend fun updateUser(user: DBEntity)

    //Delete
    @Delete
    suspend fun deleteUser(user: DBEntity)
}