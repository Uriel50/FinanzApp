package com.camu.finanzapp.movements.income

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.icu.util.Calendar
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.Spinner
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.camu.finanzapp.R
import com.camu.finanzapp.database.DataBaseApplication
import com.camu.finanzapp.database.DataBaseRepository
import com.camu.finanzapp.database.IncomeEntity
import com.camu.finanzapp.databinding.IncomeDialogBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class IncomeDialog(
    private val newIncome: Boolean = true,
    private val income: IncomeEntity = IncomeEntity(
        incomeName = "",
        incomeCategory = "",
        incomeDate = "",
        incomeMount = 0.0,
        userEmailIncome = "",
        budgetId = 1
    ),
    private val updateUI: ()->Unit,
    private val message: (String) -> Unit
):DialogFragment() {

    private var _binding : IncomeDialogBinding? = null
    private val binding get() = _binding!!

    private lateinit var builder: AlertDialog.Builder
    private lateinit var dialog: Dialog
    private var saveButton: Button? = null

    private lateinit var repository: DataBaseRepository

    private var spinnerItemSelected: String = "Salario"
    private lateinit var firebaseAuth: FirebaseAuth


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = IncomeDialogBinding.inflate(requireActivity().layoutInflater)

        val app =requireActivity().application as DataBaseApplication
        repository = app.repository

        builder = AlertDialog.Builder(requireContext())

        firebaseAuth = FirebaseAuth.getInstance()

        val singInAccount : GoogleSignInAccount? = GoogleSignIn.getLastSignedInAccount(requireContext())
        var SpinnerCategory: Spinner
        SpinnerCategory = binding.SpinnerCategory

        val categoryItems = listOf(
            IncomeItem("Salario"),
            IncomeItem("Inversiones"),
            IncomeItem("Alquileres"),
            IncomeItem("Pension"),
            IncomeItem("Regalias"),
            IncomeItem("Otros")
        )
        val adapter = IncomeCategoryAdapter(requireContext(),categoryItems)
        adapter.setDropDownViewResource(R.layout.spinner_item_movements)
        SpinnerCategory.adapter = adapter

        SpinnerCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{

            override fun onItemSelected(p0: AdapterView<*>?, view: View?, position: Int, id: Long) {
                spinnerItemSelected = categoryItems[position].text

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                spinnerItemSelected = "Salario"

            }
        }
        binding.apply {

            if (newIncome){
                editTextMountIncome.text = null
            }else{
                editTextMountIncome.setText(income.incomeMount.toString())
            }
            editTextNameIncome.setText(income.incomeName)


            val indexToSelect = categoryItems.indexOfFirst { it.text == income.incomeCategory }

            if (indexToSelect != -1){
                SpinnerCategory.setSelection(indexToSelect)
            }

        }

        dialog = if (newIncome){
            buildDialog("Guardar", "Cancelar", {
                //Create (Guardar)
                val category = spinnerItemSelected
                val date = getCurrentDateFormatted()
                val mountIncome = binding.editTextMountIncome.text.toString().toDouble()
                income.incomeName = binding.editTextNameIncome.text.toString()
                income.incomeMount = mountIncome
                income.incomeCategory = category
                income.incomeDate = date
                binding.editTextMountIncome.setOnClickListener {
                    binding.editTextMountIncome.text = null
                }
                income.incomeMount = binding.editTextMountIncome.text.toString().toDouble()

                try {
                    lifecycleScope.launch {
                        val userEmail = getUserEmail()
                        val budgetId = repository.getBudgetIdByEmail(userEmail)
                        if (budgetId != null){
                            income.budgetId = budgetId

                        }

                        repository.insertIncome(income)
                        withContext(Dispatchers.Main) {
                            updateUI()
                        }


//                        val Incomes = repository.getAllIncome().filter { it.userEmailIncome == userEmail }
//                        var totalIncomes = Incomes.sumByDouble { it.incomeMount }
//                        var Total = repository.getTotalByEmail(userEmail)
//
//                        var balanceTotal = 0.0
//                        if (Total !=null){
//                            balanceTotal = Total.balanceTotal+mountIncome
//                        }
//
//                        repository.updateBalanceTotal(balanceTotal, userEmail)
//                        repository.updateTotalIncome(totalIncomes,userEmail)


                    }
                    if (singInAccount!=null && firebaseAuth.currentUser!=null){
                        income.userEmailIncome = firebaseAuth.currentUser?.email.toString()

                    }else{
                        val userEmail = getUserEmail()

                        lifecycleScope.launch {
                            val user = repository.getUserByEmail(userEmail)
                            income.userEmailIncome = user?.userEmail.toString()
                        }
                    }

                    message("Ingreso Guardado")
                    updateUI()



                }catch(e: IOException){
                    e.printStackTrace()
                    message("Error al guardar el ingreso")
                }
            }, {
                //Cancelar
            })
        } else {
            buildDialog("Actualizar", "Borrar", {
                //Update
                val category = spinnerItemSelected
                val date = getCurrentDateFormatted()
                val mountIncome = binding.editTextMountIncome.text.toString().toDouble()
                val mountIncomeGuard = income.incomeMount
                income.incomeName = binding.editTextNameIncome.text.toString()
                income.incomeMount = mountIncome
                income.incomeCategory = category
                income.incomeDate = date

                try {
                    lifecycleScope.launch {
                        repository.updateIncome(income)

//                        val userEmail = getUserEmail()
//                        val Incomes = repository.getAllIncome().filter { it.userEmailIncome == userEmail }
//                        var totalIncomes = Incomes.sumByDouble { it.incomeMount }
//                        var Total = repository.getTotalByEmail(userEmail)
//
//                        var balanceTotal = 0.0
//                        if (Total !=null){
//                            balanceTotal = Total.balanceTotal-mountIncomeGuard+mountIncome
//                        }
//
//                        repository.updateTotalIncome(totalIncomes,userEmail)
//                        repository.updateBalanceTotal(balanceTotal,userEmail)

                    }

                    message("Ingreso actualizado exitosamente")

                    //Actualizar la UI
                    updateUI()


                }catch(e: IOException){
                    e.printStackTrace()
                    message("Error al actualizar el ingreso")

                }

            }, {
                //Delete

                AlertDialog.Builder(requireContext())
                    .setTitle("Confirmación")
                    .setMessage("¿Realmente deseas eliminar el ingreso ${income.incomeName}?")
                    .setPositiveButton("Aceptar"){ _,_ ->
                        try {
                            lifecycleScope.launch {
//                                val userEmail = getUserEmail()
//                                val Incomes = repository.getAllIncome().filter { it.userEmailIncome == userEmail }
//                                var totalIncomes = Incomes.sumByDouble { it.incomeMount }
//                                var Total = repository.getTotalByEmail(userEmail)
//
//                                var balanceTotal = 0.0
//                                if (Total !=null){
//                                    balanceTotal = Total.balanceTotal-income.incomeMount
//                                }
//
//                                repository.updateTotalIncome(totalIncomes,userEmail)
//                                repository.updateBalanceTotal(balanceTotal,userEmail)
                                repository.deleteIncome(income)


                            }

                            message("Ingreso eliminado exitosamente")

                            //Actualizar la UI
                            updateUI()

                        }catch(e: IOException){
                            e.printStackTrace()
                            message("Error al eliminar el ingreso")
                        }
                    }
                    .setNegativeButton("Cancelar"){ dialog, _ ->
                        dialog.dismiss()
                    }
                    .create()
                    .show()


            })
        }


        return dialog



    }

    fun getCurrentDateFormatted(): String {
        val calendar = Calendar.getInstance()
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH) + 1 // +1 porque enero es 0
        val year = calendar.get(Calendar.YEAR)

        return String.format("%02d/%02d/%d", day, month, year)
    }

    private fun getUserEmail(): String {
        val sharedPrefKey = "user_email"

        // Obtener el contexto de la Activity asociada
        val sharedPreferences = activity?.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        // Utilizar el contexto para acceder a SharedPreferences, si está disponible
        return sharedPreferences?.getString(sharedPrefKey, "") ?: ""
    }
    private fun buildDialog(
        btn1Text: String,
        btn2Text: String,
        positiveButton: () -> Unit,
        negativeButton: () -> Unit
    ): Dialog =
        builder.setView(binding?.root)
            .setTitle("Ingreso")
            .setPositiveButton(btn1Text, DialogInterface.OnClickListener { dialog, which ->
                //Acción para el botón positivo
                positiveButton()
            })
            .setNegativeButton(btn2Text) { _, _ ->
                //Acción para el botón negativo
                negativeButton()
            }
            .create()


}