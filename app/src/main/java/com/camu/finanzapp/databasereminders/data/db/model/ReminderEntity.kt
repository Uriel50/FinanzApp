package com.camu.finanzapp.databasereminders.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.camu.finanzapp.util.Constants

@Entity(tableName = Constants.DATABASE_NAME_TABLE_REMINDER)
data class ReminderEntity (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "reminder_id")
    val id: Long = 0,

    @ColumnInfo(name = "reminder_title")
    var title: String,

    @ColumnInfo(name = "reminder_category")
    var category: String,

    @ColumnInfo(name = "reminder_date")
    var date: String,

    @ColumnInfo(name = "hour")
    var hour: String,

    @ColumnInfo(name = "reminder_mount")
    var mount: String,

    @ColumnInfo(name = "usuario_reminder_id")
    var userReminderId: String
)
