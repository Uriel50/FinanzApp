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
    val id: Long = 0,

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
    val budgetId: Long = 0,

    @ColumnInfo(name = "user_id")
    var userId: Long,

    @ColumnInfo(name = "name_budget")
    var nameBudget: String,

    @ColumnInfo(name = "user_email_budget")
    var userEmailBudget: String
)

@Entity(tableName = Constants.DATABASE_NAME_TABLE_INCOME,
    foreignKeys = [ForeignKey(entity = BudgetEntity::class,
        parentColumns = ["budget_id"],
        childColumns = ["budget_id"],
        onDelete = ForeignKey.CASCADE)])
data class IncomeEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_income")
    val idIncome: Long = 0,

    @ColumnInfo(name = "budget_id")
    var budgetId: Long,

    @ColumnInfo(name = "income_name")
    var incomeName: String,

    @ColumnInfo(name = "income_mount")
    var incomeMount: Double,

    @ColumnInfo(name = "income_category")
    var incomeCategory: String,

    @ColumnInfo(name = "income_date")
    var incomeDate: String,

    @ColumnInfo(name = "user_email_income")
    var userEmailIncome: String
)

@Entity(tableName = Constants.DATABASE_NAME_TABLE_EXPENSE,
    foreignKeys = [ForeignKey(entity = BudgetEntity::class,
        parentColumns = ["budget_id"],
        childColumns = ["budget_id"],
        onDelete = ForeignKey.CASCADE)])
data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_expense")
    val idExpense: Long = 0,

    @ColumnInfo(name = "budget_id")
    var budgetId: Long,

    @ColumnInfo(name = "expense_name")
    var expenseName: String,

    @ColumnInfo(name = "expense_mount")
    var expenseMount: Double,

    @ColumnInfo(name = "expense_category")
    var expenseCategory: String,

    @ColumnInfo(name = "expense_date")
    var expenseDate: String,

    @ColumnInfo(name = "user_email_expense")
    var userEmailExpense: String
)

@Entity(tableName = Constants.DATABASE_NAME_TABLE_TOTAL,
    foreignKeys = [ForeignKey(entity = BudgetEntity::class,
        parentColumns = ["budget_id"],
        childColumns = ["budget_id"],
        onDelete = ForeignKey.CASCADE)])
data class TotalsEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_total")
    val idTotal: Long = 0,

    @ColumnInfo(name = "budget_id")
    var budgetId: Long,

    @ColumnInfo(name = "total_income")
    var totalIncome: Double,

    @ColumnInfo(name = "total_expense")
    var totalExpense: Double,

    @ColumnInfo(name = "balance_total")
    var balanceTotal: Double,

    @ColumnInfo(name = "user_email_total")
    var userEmailTotal: String
)

@Entity(tableName = Constants.DATABASE_NAME_TABLE_REMINDER,
    foreignKeys = [ForeignKey(entity = UserEntity::class,
        parentColumns = ["user_id"],
        childColumns = ["user_id"],
        onDelete = ForeignKey.CASCADE)])
data class ReminderEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "reminder_id")
    val reminderId: Long = 0,

    @ColumnInfo(name = "user_id")
    var userId: Long,

    @ColumnInfo(name = "reminder_title")
    var reminderTitle: String,

    @ColumnInfo(name = "reminder_category")
    var reminderCategory: String,

    @ColumnInfo(name = "reminder_date")
    var reminderDate: String,

    @ColumnInfo(name = "hour")
    var hour: String,

    @ColumnInfo(name = "reminder_mount")
    var reminderMount: Double,

    @ColumnInfo(name = "user_email_reminder")
    var userEmailReminder: String


)
