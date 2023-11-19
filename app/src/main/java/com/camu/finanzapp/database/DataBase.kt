package com.camu.finanzapp.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.camu.finanzapp.util.Constants
import android.content.Context


// Anotación @Database define la base de datos de Room con las entidades y la versión 1
@Database(
    entities = [UserEntity::class, BudgetEntity::class, IncomeEntity::class, ExpenseEntity::class, TotalsEntity::class, ReminderEntity::class],
    version = 1,
    exportSchema = true
)
// Clase abstracta que representa la base de datos de Room
abstract class DataBase : RoomDatabase() {

    // Métodos abstractos que devuelven objetos DAO para interactuar con la base de datos
    abstract fun userDao(): UserDao
    abstract fun budgetDao(): BudgetDao
    abstract fun incomeDao(): IncomeDao
    abstract fun expenseDao(): ExpenseDao
    abstract fun totalDao(): TotalDao
    abstract fun reminderDao(): ReminderDao

    companion object {
        @Volatile
        private var INSTANCE: DataBase? = null

        // Método estático para obtener una instancia de la base de datos
        fun getDataBase(context: Context): DataBase {
            return INSTANCE ?: synchronized(this) {
                // Se crea una instancia de la base de datos utilizando Room
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DataBase::class.java,
                    Constants.DATABASE_NAME_FA
                ).fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}