package com.camu.finanzapp.databasereminders.application

import android.app.Application
import com.camu.finanzapp.databasereminders.data.ReminderRepository
import com.camu.finanzapp.databasereminders.data.db.ReminderDatabase

class RemindersDBApp(): Application() {

    private val database by lazy {
        ReminderDatabase.getDatabase(this@RemindersDBApp)
    }

    val repository by lazy {
        ReminderRepository(database.ReminderDao())
    }
}