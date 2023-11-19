package com.camu.finanzapp.database

import androidx.room.*
import com.camu.finanzapp.util.Constants.DATABASE_NAME_TABLE_USER

@Dao
interface UserDao {

    @Insert
    suspend fun insertUser(user: UserEntity)

    //Read
    @Query("SELECT * FROM ${DATABASE_NAME_TABLE_USER}")
    suspend fun getUser(): List<UserEntity>

    @Query("SELECT * FROM ${DATABASE_NAME_TABLE_USER}")
    suspend fun getAllUsers(): List<UserEntity>

    @Query("SELECT * FROM ${DATABASE_NAME_TABLE_USER} WHERE user_email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): UserEntity?
    @Update
    suspend fun updateUser(user: UserEntity)

    @Delete
    suspend fun deleteUser(user: UserEntity)
}
