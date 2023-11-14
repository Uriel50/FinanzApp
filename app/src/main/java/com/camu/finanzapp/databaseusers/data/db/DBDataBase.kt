package com.camu.finanzapp.databaseusers.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.camu.finanzapp.databaseusers.data.db.model.DBEntity
import com.camu.finanzapp.util.Constants

// Anotación @Database define la base de datos de Room con la entidad DBEntity y la versión 1
@Database(
    entities = [DBEntity::class],
    version = 1,
    exportSchema = true
)

// Clase abstracta que representa la base de datos de Room
abstract class DBDataBase : RoomDatabase() {

    // Método abstracto que devuelve un objeto DBDao para interactuar con la base de datos
    abstract fun userDao(): DBDao

    companion object {
        @Volatile
        private var INSTANCE: DBDataBase? = null

        // Método estático para obtener una instancia de la base de datos
        fun getDataBase(context: Context): DBDataBase {
            return INSTANCE ?: synchronized(this) {
                // Se crea una instancia de la base de datos utilizando Room
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DBDataBase::class.java,
                    Constants.DATABASE_NAME
                ).fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
