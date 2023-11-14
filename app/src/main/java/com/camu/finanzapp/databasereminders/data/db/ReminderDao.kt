package com.camu.finanzapp.databasereminders.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.camu.finanzapp.databasereminders.data.db.model.ReminderEntity
import com.camu.finanzapp.util.Constants.DATABASE_NAME_TABLE_REMINDER

@Dao
interface ReminderDao {

    //Crate
    @Insert
    suspend fun insertReminder(reminder: ReminderEntity)

    @Insert
    suspend fun insertReminder(reminders: List<ReminderEntity>)

    @Query("SELECT * FROM ${DATABASE_NAME_TABLE_REMINDER}")
    suspend fun getAllReminders(): List<ReminderEntity>

    @Update
    suspend fun updateReminder(reminder: ReminderEntity)

    @Delete
    suspend fun deleteReminder(reminders: ReminderEntity)
}