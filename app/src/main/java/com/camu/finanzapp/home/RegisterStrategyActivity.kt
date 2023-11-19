package com.camu.finanzapp.home

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.camu.finanzapp.R
import com.camu.finanzapp.databasefinance.application.FinanceDBApp
import com.camu.finanzapp.databasefinance.data.FinanceRepository
import com.camu.finanzapp.databasefinance.data.db.model.GastoEntity
import com.camu.finanzapp.databasefinance.data.db.model.IngresoEntity
import com.camu.finanzapp.databasefinance.data.db.model.TotalEntity
import com.camu.finanzapp.databasefinance.data.db.model.UsuarioEntity
import com.camu.finanzapp.databinding.ActivityRegisterStrategyBinding
import com.camu.finanzapp.login.LoginFragment
import com.camu.finanzapp.util.Constants
import com.camu.finanzapp.util.GlobalData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class RegisterStrategyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterStrategyBinding
    private lateinit var repository: FinanceRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterStrategyBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Obtener la instancia del repositorio
        val application = applicationContext as FinanceDBApp
        repository = application.repository

        binding.buttonRegister.setOnClickListener {
            val userEmail = getUserEmail()
            val nameBudget = binding.amountEditText.text.toString()
            val balanceTotal = binding.amountEditText.text.toString().toDoubleOrNull() ?: 0.0

            // Crear y guardar el nuevo usuario
            val newUser = UsuarioEntity(
                userFinanceId = userEmail,
                nameBudget = nameBudget
            )
            // Llamar a saveUser desde una coroutine
            CoroutineScope(Dispatchers.IO).launch {
                val result = saveUser(newUser, balanceTotal)
                val alertDialog : AlertDialog

                if (result) {
                    GlobalData.isStrategy=true
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
                        .setMessage("Ha ocurrido un error. No se creo el presupuesto")
                        .setTitle("Error")
                        .setPositiveButton("Aceptar") { dialog, _ ->
                            dialog.dismiss()

                        }
                        .create()
                    alertDialog.show()
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
    private suspend fun saveUser(usuario: UsuarioEntity, balanceTotal: Double): Boolean {
        try {
            // Insertar usuario y obtener el ID generado
            repository.insertUsuario(usuario)
            val email = getUserEmail()
            val user = repository?.getUserByEmail(email)

            if (user != null) {
                // Crear y guardar las entradas por defecto en Ingresos y Gastos
                val defaultIngreso = IngresoEntity(
                    userId = user.id,
                    nombre = "No Existen ingresos",
                    monto = 0.0,
                    categoria = "otros",
                    fecha = "00/00/0000"
                )
                repository.insertIngreso(defaultIngreso)

                val defaultGasto = GastoEntity(
                    userId = user.id,
                    nombre = "No Existen gastos",
                    monto = 0.0,
                    categoria = "otros",
                    fecha = "00/00/0000"
                )
                repository.insertGasto(defaultGasto)

                // Crear y guardar la entrada por defecto en Total
                val defaultTotal = TotalEntity(
                    userId = user.id,
                    totalIngresos = 0.0,
                    totalGastos = 0.0,
                    balanceTotal = balanceTotal // Asumiendo que este valor viene del formulario
                )
                repository.insertTotal(defaultTotal)

                // Datos agregados correctamente
                return true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // Hubo un error en el proceso
        return false
    }



}