package com.camu.finanzapp.database

import androidx.lifecycle.LiveData
import androidx.room.Transaction

class DataBaseRepository(
    private val userDao: UserDao,
    private val totalDao: TotalDao,
    private val reminderDao: ReminderDao,
    private val incomeDao: IncomeDao,
    private val expenseDao: ExpenseDao,
    private val budgetDao: BudgetDao
) {


    //User operations*****************************************************
    // Función suspendida para insertar un usuario en la base de datos
    suspend fun insertUser(user: UserEntity) {
        userDao.insertUser(user)
    }

    // Función suspendida para obtener la lista de usuarios desde la base de datos
    suspend fun getUser(): List<UserEntity> = userDao.getUser()

    suspend fun getUserIdByEmail(email: String): Long? {
        return userDao.getUserIdByEmail(email)
    }
    suspend fun getAllUsers(): List<UserEntity> = userDao.getAllUsers()

    suspend fun getUserByEmail(email: String):UserEntity? {
        return userDao.getUserByEmail(email)
    }


    // Función suspendida para actualizar un usuario en la base de datos
    suspend fun updateUser(user: UserEntity) {
        userDao.updateUser(user)
    }

    // Función suspendida para eliminar un usuario de la base de datos
    suspend fun deleteUser(user: UserEntity) {
        userDao.deleteUser(user)
    }

    //Income operations *****************************************************

    suspend fun insertIncome(income: IncomeEntity) {
        incomeDao.insertIncome(income)
    }

    fun getAllIncomeLiveData(email: String): LiveData<List<IncomeEntity>> = incomeDao.getAllIncomeLiveData(email)

    suspend fun getAllIncome(): List<IncomeEntity> = incomeDao.getAllIncome()

    suspend fun updateIncome(income:IncomeEntity) {
        incomeDao.updateIncome(income)
    }

    suspend fun deleteIncome(income: IncomeEntity) {
        incomeDao.deleteIncome(income)
    }
    suspend fun getTotalIncomeByEmail(email: String): Double {
        return incomeDao.getTotalIncomeByEmail(email) ?: 0.0
    }

    suspend fun getIncomesByCategory(email: String, category: String): Double {
        return incomeDao.getIncomesByCategory(email, category)
    }

    suspend fun getIncomesByMonth(email: String, month: String): Double {
        return incomeDao.getIncomesByMonth(email, month)
    }

    //Expense operations ****************************************************

    suspend fun insertExpense(expense: ExpenseEntity) {
        expenseDao.insertExpense(expense)
    }

    suspend fun getAllExpenses(): List<ExpenseEntity> = expenseDao.getAllExpenses()

    suspend fun updateExpense(expense: ExpenseEntity) {
        expenseDao.updateExpense(expense)
    }

    suspend fun deleteExpense(expense: ExpenseEntity) {
        expenseDao.deleteExpense(expense)
    }
    suspend fun getTotalExpenseByEmail(email: String): Double {
        return expenseDao.getTotalExpenseByEmail(email) ?: 0.0
    }

    fun getAllExpenseLiveData(email: String): LiveData<List<ExpenseEntity>> = expenseDao.getAllExpenseLiveData(email)


    suspend fun getExpensesByCategory(email: String, category: String): Double {
        return expenseDao.getExpensesByCategory(email, category)
    }
    suspend fun getExpensesByMonth(email: String, month: String): Double {
        return expenseDao.getExpensesByMonth(email, month)
    }


    //Reminder operations ****************************************************

    suspend fun insertReminder(reminder: ReminderEntity){
        reminderDao.insertReminder(reminder)
    }

    

    suspend fun getAllReminders(): List<ReminderEntity> = reminderDao.getAllReminders()


    suspend fun updateReminder(reminder: ReminderEntity){
        reminderDao.updateReminder(reminder)
    }

    suspend fun deleteReminder(reminder: ReminderEntity){
        reminderDao.deleteReminder(reminder)
    }

    //Budget operations ****************************************************************************

    suspend fun insertBudget(budget:BudgetEntity){
        budgetDao.insertBudget(budget)
    }
    suspend fun getAllBudgets(): List<BudgetEntity> = budgetDao.getAllBudgets()

    suspend fun getBudgetIdByEmail(email: String): Long? {
        return budgetDao.getBudgetIdByEmail(email)
    }
    suspend fun getBudgetNameByEmail(id_user: String): String? {
        return budgetDao.getNameBudgetByEmail(id_user)
    }

    suspend fun getBudgetByEmail(email: String): BudgetEntity {
        return budgetDao.getBudgetByEmail(email)
    }
    suspend fun updateBudget(budget: BudgetEntity){
        budgetDao.updateBudget(budget)
    }

    suspend fun deleteBudget(budget: BudgetEntity){
        budgetDao.deleteBudget(budget)

    }
    fun getBudgetByEmailLiveData(email: String): LiveData<BudgetEntity> {
        return budgetDao.getBudgetByEmailLiveData(email)
    }
    fun getBudgetIdByEmailLiveData(email: String): Long? {
        return budgetDao.getBudgetIdByEmailLiveData(email)
    }



    //Total operations *****************************************************************************

    suspend fun insertTotal(total: TotalsEntity){
        totalDao.insertTotal(total)
    }
    suspend fun getAllTotals(): List<TotalsEntity> = totalDao.getAllTotals()

    suspend fun updateTotal(total: TotalsEntity){
        totalDao.updateTotals(total)
    }

    suspend fun deleteTotal(total: TotalsEntity){
        totalDao.deleteTotals(total)
    }
    suspend fun getTotalById(idTotal: Long): TotalsEntity? {
        return totalDao.getTotalById(idTotal)
    }

    @Transaction
    suspend fun getTotalByEmail(email: String): TotalsEntity? {
        return totalDao.getTotalByEmail(email)
    }

    @Transaction
    suspend fun updateTotalIncome(newTotalIncome: Double, email: String) {
        totalDao.updateTotalIncome(newTotalIncome, email)
    }

    @Transaction
    suspend fun updateTotalExpense(newTotalExpense: Double, email: String) {
        totalDao.updateTotalExpense(newTotalExpense, email)
    }

    @Transaction
    suspend fun updateBalanceTotal(newBalanceTotal: Double, email: String) {
        totalDao.updateBalanceTotal(newBalanceTotal, email)
    }
    fun getTotalByEmailLiveData(email: String): LiveData<TotalsEntity> {
        return totalDao.getTotalByEmailLiveData(email)
    }


}
