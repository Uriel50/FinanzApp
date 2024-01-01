package com.camu.finanzapp.home

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.camu.finanzapp.database.BudgetEntity
import com.camu.finanzapp.database.DataBaseApplication
import com.camu.finanzapp.database.DataBaseRepository
import com.camu.finanzapp.database.ExpenseEntity
import com.camu.finanzapp.database.IncomeEntity
import com.camu.finanzapp.database.TotalsEntity
import com.camu.finanzapp.databinding.ActivityRegisterStrategyBinding
import com.camu.finanzapp.util.GlobalData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterStrategyActivity(
    private val newUser: BudgetEntity = BudgetEntity(
        userEmailBudget = "",
        nameBudget = "",
        userId = 1
    ),
    private val income: IncomeEntity = IncomeEntity(
        budgetId = 0 ,
        incomeName = "",
        incomeMount = 0.0,
        incomeCategory = "",
        incomeDate = "",
        userEmailIncome = ""
    ),
    private val expense: ExpenseEntity = ExpenseEntity(
        budgetId = 0,
        expenseName = "",
        expenseMount = 0.0,
        expenseCategory = "",
        expenseDate = "",
        userEmailExpense = ""
    ),
    private val total: TotalsEntity = TotalsEntity(
        budgetId = 0,
        totalIncome = 0.0,
        totalExpense = 0.0,
        balanceTotal = 0.0,
        userEmailTotal = ""
    )

) : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterStrategyBinding
    private lateinit var repository: DataBaseRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterStrategyBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Obtener la instancia del repositorio
        val application = applicationContext as DataBaseApplication
        repository = application.repository

        binding.buttonRegister.setOnClickListener {
            val nameBudget = binding.nameEditText.text.toString()
            val balanceTotal = binding.amountEditText.text.toString().toDouble()


            // Llamar a saveUser desde una coroutine
            CoroutineScope(Dispatchers.IO).launch {
                val result = saveMovements(nameBudget,balanceTotal)

                withContext(Dispatchers.Main) {
                    val alertDialog: AlertDialog
                    if (result) {
                        GlobalData.isStrategy = true
                        alertDialog = AlertDialog.Builder(this@RegisterStrategyActivity)
                            .setMessage("Comienza el control de tus finanzas")
                            .setTitle("Presupuesto creado correctamente")
                            .setPositiveButton("Aceptar") { dialog, _ ->
                                dialog.dismiss()
                                val intent = Intent(this@RegisterStrategyActivity, HomeActivity::class.java)
                                startActivity(intent)
                            }
                            .create()
                        alertDialog.show()
                    } else {
                        // Hubo un error, puedes mostrar un mensaje de error
                        GlobalData.isStrategy = false
                        alertDialog = AlertDialog.Builder(this@RegisterStrategyActivity)
                            .setMessage("Ha ocurrido un error. No se creó el presupuesto")
                            .setTitle("Error")
                            .setPositiveButton("Aceptar") { dialog, _ ->
                                dialog.dismiss()
                            }
                            .create()
                        alertDialog.show()
                    }
                }
            }


        }



        val backButton = binding.backButtonBudget

        

        backButton.setOnClickListener {
            onBackPressed()
        }

    }

    private fun getUserEmail(): String {
        val sharedPrefKey = "user_email"
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString(sharedPrefKey, "") ?: ""
    }

    private suspend fun saveMovements(nameBudget:String, balanceTotal: Double): Boolean {
        try {

            lifecycleScope.launch {
                val userEmail = getUserEmail()
                val userId = repository.getUserIdByEmail(userEmail)
                if (userId != null){
                    newUser.userId = userId
                    newUser.nameBudget = nameBudget
                    newUser.userEmailBudget = userEmail
                }

                repository.insertBudget(newUser)
                val user = repository?.getUserByEmail(userEmail)
                val idBudgetByEmail = repository.getBudgetIdByEmail(userEmail)
                if (user != null){
                    if (idBudgetByEmail != null){

                        total.budgetId = idBudgetByEmail
                        total.totalIncome = 0.0
                        total.totalExpense = 0.0
                        total.balanceTotal = balanceTotal
                        total.userEmailTotal = userEmail

                        repository.insertTotal(total)
                    }

                }
            }
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            runOnUiThread {
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
        // Hubo un error en el proceso
        return false
    }



}