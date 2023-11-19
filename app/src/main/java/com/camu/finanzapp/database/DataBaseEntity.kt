package com.camu.finanzapp.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.camu.finanzapp.util.Constants


@Entity(tableName = Constants.DATABASE_NAME_TABLE_USER)
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "user_id")
    val userId: Long,

    @ColumnInfo(name = "user_nickname")
    val userNickname: String,
    @ColumnInfo(name = "user_name")
    val userName: String,

    @ColumnInfo(name = "user_lastname")
    val userLastname: String,

    @ColumnInfo(name = "user_sex")
    val userSex: String,

    @ColumnInfo(name = "user_email")
    val userEmail: String,

    @ColumnInfo(name = "user_key")
    val userKey: String
)

@Entity(tableName = Constants.DATABASE_NAME_TABLE_BUDGET,
    foreignKeys = [ForeignKey(entity = UserEntity::class,
        parentColumns = ["user_id"],
        childColumns = ["user_id"],
        onDelete = ForeignKey.CASCADE)])
data class BudgetEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "budget_id")
    val budgetId: Long,

    @ColumnInfo(name = "user_id")
    val userId: Long,

    @ColumnInfo(name = "name_budget")
    val nameBudget: String
)

@Entity(tableName = Constants.DATABASE_NAME_TABLE_INCOME,
    foreignKeys = [ForeignKey(entity = BudgetEntity::class,
        parentColumns = ["budget_id"],
        childColumns = ["budget_id"],
        onDelete = ForeignKey.CASCADE)])
data class IncomeEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_income")
    val idIncome: Long,

    @ColumnInfo(name = "budget_id")
    val budgetId: Long,

    @ColumnInfo(name = "income_name")
    val incomeName: String,

    @ColumnInfo(name = "income_mount")
    val incomeMount: Double,

    @ColumnInfo(name = "income_category")
    val incomeCategory: String,

    @ColumnInfo(name = "income_date")
    val incomeDate: String
)

@Entity(tableName = Constants.DATABASE_NAME_TABLE_EXPENSE,
    foreignKeys = [ForeignKey(entity = BudgetEntity::class,
        parentColumns = ["budget_id"],
        childColumns = ["budget_id"],
        onDelete = ForeignKey.CASCADE)])
data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_expense")
    val idExpense: Long,

    @ColumnInfo(name = "budget_id")
    val budgetId: Long,

    @ColumnInfo(name = "expense_name")
    val expenseName: String,

    @ColumnInfo(name = "expense_mount")
    val expenseMount: Double,

    @ColumnInfo(name = "expense_category")
    val expenseCategory: String,

    @ColumnInfo(name = "expense_date")
    val expenseDate: String
)

@Entity(tableName = Constants.DATABASE_NAME_TABLE_TOTAL,
    foreignKeys = [ForeignKey(entity = BudgetEntity::class,
        parentColumns = ["budget_id"],
        childColumns = ["budget_id"],
        onDelete = ForeignKey.CASCADE)])
data class TotalsEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_total")
    val idTotal: Long,

    @ColumnInfo(name = "budget_id")
    val budgetId: Long,

    @ColumnInfo(name = "total_income")
    val totalIncome: Double,

    @ColumnInfo(name = "total_expense")
    val totalExpense: Double,

    @ColumnInfo(name = "balance_total")
    val balanceTotal: Double
)

@Entity(tableName = Constants.DATABASE_NAME_TABLE_REMINDER,
    foreignKeys = [ForeignKey(entity = UserEntity::class,
        parentColumns = ["user_id"],
        childColumns = ["user_id"],
        onDelete = ForeignKey.CASCADE)])
data class ReminderEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "reminder_id")
    val reminderId: Long,

    @ColumnInfo(name = "user_id")
    val userId: Long,

    @ColumnInfo(name = "reminder_title")
    val reminderTitle: String,

    @ColumnInfo(name = "reminder_category")
    val reminderCategory: String,

    @ColumnInfo(name = "reminder_date")
    val reminderDate: String,

    @ColumnInfo(name = "hour")
    val hour: String,

    @ColumnInfo(name = "reminder_mount")
    val reminderMount: Double
)
