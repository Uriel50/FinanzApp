package com.camu.finanzapp.movements.expense

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
import com.camu.finanzapp.database.DataBase
import com.camu.finanzapp.database.DataBaseApplication
import com.camu.finanzapp.database.DataBaseRepository
import com.camu.finanzapp.database.ExpenseEntity
import com.camu.finanzapp.databinding.ExpenseDialogBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class ExpenseDialog (
    private val newExpense: Boolean = true,
    private val expense: ExpenseEntity = ExpenseEntity(
        expenseName = "",
        expenseCategory = "",
        expenseDate = "",
        expenseMount = 0.0,
        userEmailExpense = "",
        budgetId = 1
    ),
    private val updateUI: ()-> Unit,
    private val message: (String) -> Unit
):DialogFragment() {

    private var _binding : ExpenseDialogBinding? = null
    private val binding get() = _binding!!

    private lateinit var builder: AlertDialog.Builder
    private lateinit var dialog: Dialog
    private var saveButton: Button? = null

    private lateinit var repository: DataBaseRepository
    private lateinit var database: DataBase
    private var spinnerItemSelected: String = "Comida"
    private lateinit var firebaseAuth: FirebaseAuth
    private val expenseCoroutineScope = CoroutineScope(Dispatchers.IO)

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = ExpenseDialogBinding.inflate(requireActivity().layoutInflater)

        database = DataBase.getDataBase(requireContext())
        repository = DataBaseRepository(
            database.userDao(),
            database.totalDao(),
            database.reminderDao(),
            database.incomeDao(),
            database.expenseDao(),
            database.budgetDao()
        )

        builder = AlertDialog.Builder(requireContext())

        firebaseAuth = FirebaseAuth.getInstance()



        val singInAccount : GoogleSignInAccount? = GoogleSignIn.getLastSignedInAccount(requireContext())
        var SpinnerCategory: Spinner
        SpinnerCategory = binding.SpinnerCategoryExpense

        val categoryItems = listOf(
            ExpenseItem("Comida"),
            ExpenseItem("Transporte"),
            ExpenseItem("Vivienda"),
            ExpenseItem("Entretenimiento"),
            ExpenseItem("Salud"),
            ExpenseItem("Educación"),
            ExpenseItem("Impuestos"),
            ExpenseItem("Viajes"),
            ExpenseItem("Otros")
        )
        val adapter = ExpenseCategoryAdapter(requireContext(), categoryItems)
        adapter.setDropDownViewResource(R.layout.spinner_item_movements)
        SpinnerCategory.adapter = adapter

        SpinnerCategory.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{

            override fun onItemSelected(p0: AdapterView<*>?, view: View?, position: Int, id: Long) {
                spinnerItemSelected = categoryItems[position].text

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                spinnerItemSelected = "Comida"

            }
        }
        binding.apply {

            if (newExpense){
                editTextMountExpense.text = null
            }else{
                editTextMountExpense.setText(expense.expenseMount.toString())
            }

            editTextNameExpense.setText(expense.expenseName)


            val indexToSelect = categoryItems.indexOfFirst { it.text == expense.expenseCategory }
            if (indexToSelect != -1){
                SpinnerCategory.setSelection(indexToSelect)
            }
        }

        dialog = if (newExpense){
            buildDialog("Guardar", "Cancelar", {
                //Create (Guardar)
                val category = spinnerItemSelected
                val date = getCurrentDateFormatted()
                val mountExpense = binding.editTextMountExpense.text.toString().toDouble()
                expense.expenseName = binding.editTextNameExpense.text.toString()
                expense.expenseCategory = category
                expense.expenseDate = date
                expense.expenseMount = mountExpense

                try {


                    expenseCoroutineScope.launch  {
                        val userEmail = getUserEmail()
                        val budgetId = repository.getBudgetIdByEmail(userEmail)
                        if (budgetId != null){
                            expense.budgetId = budgetId

                        }
                        repository.insertExpense(expense)

                        val expenseSum = repository.getTotalExpenseByEmail(userEmail)
                        val total = repository.getTotalByEmail(userEmail)


                        if (total != null) repository.updateBalanceTotal(total.balanceTotal-mountExpense,userEmail)

                        repository.updateTotalExpense(expenseSum,userEmail)


                        withContext(Dispatchers.Main) {
                            updateUI()
                        }
//                        val Expenses = repository.getAllExpenses().filter { it.userEmailExpense == userEmail }
//                        var totatExpenses = Expenses.sumByDouble { it.expenseMount }
//                        var Total = repository.getTotalByEmail(userEmail)
//
//                        var balanceTotal = 0.0
//                        if (Total !=null){
//                            balanceTotal = Total.balanceTotal-mountExpense
//                        }
//                        repository.updateTotalExpense(totatExpenses,userEmail)
//                        repository.updateBalanceTotal(balanceTotal,userEmail)
                    }
                    if (singInAccount!=null && firebaseAuth.currentUser!=null){
                        expense.userEmailExpense = firebaseAuth.currentUser?.email.toString()

                    }else{
                        val userEmail = getUserEmail()

                        expenseCoroutineScope.launch {
                            val user = repository.getUserByEmail(userEmail)
                            expense.userEmailExpense = user?.userEmail.toString()
                        }
                    }

                    message("Gasto Guardado")
                    updateUI()

                }catch(e: IOException){
                    e.printStackTrace()
                    message("Error al guardar el gasto")
                }
            }, {
                //Cancelar
            })
        } else {
            buildDialog("Actualizar", "Borrar", {
                //Update
                val category = spinnerItemSelected
                val date = getCurrentDateFormatted()
                val mountExpenseGuard = expense.expenseMount
                val mountExpense = binding.editTextMountExpense.text.toString().toDouble()
                expense.expenseName = binding.editTextNameExpense.text.toString()
                expense.expenseMount = mountExpense
                expense.expenseCategory = category
                expense.expenseDate = date

                try {
                    expenseCoroutineScope.launch {
                        repository.updateExpense(expense)
                        val userEmail = getUserEmail()

//                        val Expenses = repository.getAllExpenses().filter { it.userEmailExpense == userEmail }
//                        var totatExpenses = Expenses.sumByDouble { it.expenseMount }
//                        var Total = repository.getTotalByEmail(userEmail)
//
//
//
//                        var balanceTotal = 0.0
//                        if (Total !=null){
//                            balanceTotal = Total.balanceTotal+mountExpenseGuard-mountExpense
//                        }
//
//                        repository.updateTotalExpense(totatExpenses,userEmail)
//                        repository.updateBalanceTotal(balanceTotal,userEmail)




                    }

                    message("Gasto actualizado exitosamente")

                    //Actualizar la UI
                    updateUI()


                }catch(e: IOException){
                    e.printStackTrace()
                    message("Error al actualizar el gasto")

                }

            }, {
                //Delete

                AlertDialog.Builder(requireContext())
                    .setTitle("Confirmación")
                    .setMessage("¿Realmente deseas eliminar el gasto ${expense.expenseName}?")
                    .setPositiveButton("Aceptar"){ _,_ ->
                        try {
                            expenseCoroutineScope.launch {


//                                val userEmail = getUserEmail()
//
//                                val Expenses = repository.getAllExpenses().filter { it.userEmailExpense == userEmail }
//                                var totatExpenses = Expenses.sumByDouble { it.expenseMount }
//                                var Total = repository.getTotalByEmail(userEmail)
//
//                                var balanceTotal = 0.0
//                                if (Total !=null){
//                                    balanceTotal = Total.balanceTotal+expense.expenseMount
//                                }
//                                repository.updateTotalExpense(totatExpenses,userEmail)
                                repository.deleteExpense(expense)
                            }

                            message("Gasto eliminado exitosamente")

                            //Actualizar la UI
                            updateUI()

                        }catch(e: IOException){
                            e.printStackTrace()
                            message("Error al eliminar el gasto")
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

    override fun onDestroy() {
        expenseCoroutineScope.coroutineContext.cancelChildren()
        super.onDestroy()
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
            .setTitle("Gasto")
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