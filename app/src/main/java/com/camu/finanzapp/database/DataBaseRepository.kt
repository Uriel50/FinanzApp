package com.camu.finanzapp.database

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

    suspend fun getUserByEmail(email: String): UserEntity? {
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

    suspend fun getAllIncome(): List<IncomeEntity> = incomeDao.getAllIncome()

    suspend fun updateIncome(income: IncomeEntity) {
        incomeDao.updateIncome(income)
    }

    suspend fun deleteIncome(income: IncomeEntity) {
        incomeDao.deleteIncome(income)
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

    //Reminder operations ****************************************************

    suspend fun insertReminder(reminder: ReminderEntity){
        reminderDao.insertReminder(reminder)
    }

    suspend fun insertReminder(title: String, category:String, date:String,hour: String, mount: Double, userId: Long, userEmail: String){
        reminderDao.insertReminder(ReminderEntity(reminderTitle = title, reminderCategory = category, reminderDate = date, reminderMount = mount, hour = hour, userId = userId, userEmailReminder = userEmail))
    }



    suspend fun getAllReminders(): List<ReminderEntity> = reminderDao.getAllReminders()


    suspend fun updateReminder(reminder: ReminderEntity){
        reminderDao.updateReminder(reminder)
    }

    suspend fun deleteReminder(reminder: ReminderEntity){
        reminderDao.deleteReminder(reminder)
    }

    //Budget operations ****************************************************************************

    suspend fun inserBudget(budget: BudgetEntity){
        budgetDao.insertBudget(budget)
    }
    suspend fun getAllBudgets(): List<BudgetEntity> = budgetDao.getAllBudgets()

    suspend fun getBudgetIdByEmail(email: String): Long? {
        return budgetDao.getBudgetIdByEmail(email)
    }
    suspend fun getBudgetNameByEmail(id_user: String): String? {
        return budgetDao.getNameBudgetByEmail(id_user)
    }

    suspend fun getBudgetByEmail(email: String): BudgetEntity? {
        return budgetDao.getBudgetByEmail(email)
    }
    suspend fun updateBudget(budget: BudgetEntity){
        budgetDao.updateBudget(budget)
    }

    suspend fun deleteBudget(budget: BudgetEntity){
        budgetDao.deleteBudget(budget)
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

    suspend fun getTotalByEmail(email: String): TotalsEntity? {
        return totalDao.getTotalByEmail(email)
    }

    suspend fun updateTotalIncome(newTotalIncome: Double, email: String) {
        totalDao.updateTotalIncome(newTotalIncome, email)
    }

    suspend fun updateTotalExpense(newTotalExpense: Double, email: String) {
        totalDao.updateTotalExpense(newTotalExpense, email)
    }

    suspend fun updateBalanceTotal(newBalanceTotal: Double, email: String) {
        totalDao.updateBalanceTotal(newBalanceTotal, email)
    }
}
