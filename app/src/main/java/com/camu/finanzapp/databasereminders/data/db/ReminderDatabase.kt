package com.camu.finanzapp.databasereminders.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.camu.finanzapp.databasereminders.data.db.model.ReminderEntity
import com.camu.finanzapp.util.Constants

@Database(
    entities = [ReminderEntity::class],
    version = 1,
    exportSchema = true
)
abstract class ReminderDatabase: RoomDatabase() {

    abstract fun ReminderDao(): ReminderDao

    companion object{
        @Volatile
        private var INSTANCE : ReminderDatabase? = null

        fun getDatabase(context: Context): ReminderDatabase {
            return INSTANCE?: synchronized(this){
                //Si la instancia no es nula, entonces se regresa
                // si es nula, entonces se crea la base de datos (patrón singleton)
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ReminderDatabase::class.java,
                    Constants.DATABASE_NAME_REMINDER
                ).fallbackToDestructiveMigration() //Permite a Room recrear las tablas de la BD si las migraciones no se encuentran
                    .build()

                INSTANCE = instance

                instance
            }
        }
    }
}