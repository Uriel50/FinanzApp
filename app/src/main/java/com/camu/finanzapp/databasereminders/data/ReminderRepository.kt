package com.camu.finanzapp.databasereminders.data

import com.camu.finanzapp.databasereminders.data.db.ReminderDao
import com.camu.finanzapp.databasereminders.data.db.model.ReminderEntity

class ReminderRepository (private val ReminderDao:ReminderDao){

    suspend fun insertReminder(reminder: ReminderEntity){
        ReminderDao.insertReminder(reminder)
    }

    suspend fun insertReminder(title: String, category:String, date:String,hour: String, mount: String, userReminderId:String){
        ReminderDao.insertReminder(ReminderEntity(title=title, category = category, date = date, hour = hour, mount = mount, userReminderId =userReminderId ))
    }

    suspend fun getAllReminders(): List<ReminderEntity> = ReminderDao.getAllReminders()

    suspend fun updateReminder(reminder: ReminderEntity){
        ReminderDao.updateReminder(reminder)
    }

    suspend fun deleteReminder(reminder: ReminderEntity){
        ReminderDao.deleteReminder(reminder)
    }
}