package com.camu.finanzapp.movements.expense

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.camu.finanzapp.R
import com.camu.finanzapp.database.BudgetEntity
import com.camu.finanzapp.database.DataBase
import com.camu.finanzapp.database.DataBaseRepository
import com.camu.finanzapp.database.ExpenseEntity
import com.camu.finanzapp.database.IncomeEntity
import com.camu.finanzapp.databinding.FragmentExpenseMovementsBinding
import com.camu.finanzapp.movements.FinanzappViewModel
import com.camu.finanzapp.movements.income.IncomeDialog
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class ExpenseMovementsFragment : Fragment(R.layout.fragment_expense_movements) {

    private lateinit var binding: FragmentExpenseMovementsBinding
    private lateinit var database: DataBase
    private lateinit var repository: DataBaseRepository
    private var expenses: List<ExpenseEntity> = emptyList()
    private var isStrategy: BudgetEntity?=null
    private val finanzappViewModel: FinanzappViewModel by viewModels()

    private lateinit var expenseAdapter: ExpenseAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentExpenseMovementsBinding.bind(view)

        database = DataBase.getDataBase(requireContext())
        repository = DataBaseRepository(
            database.userDao(),
            database.totalDao(),
            database.reminderDao(),
            database.incomeDao(),
            database.expenseDao(),
            database.budgetDao()
        )

        expenseAdapter = ExpenseAdapter (){expense ->
            expenseClicked(expense)

        }

        binding.listExpenseMovements.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = expenseAdapter

        }

        updateUI()

        binding.buttonNewExpenseMovements.setOnClickListener {
            val dialog = ExpenseDialog (updateUI = {
                updateUI()
            }, message = { text ->
                message(text)
            })
            dialog.show(parentFragmentManager,"dialog")
        }


    }

    private fun expenseClicked(expense: ExpenseEntity){
        //Toast.makeText(this, "Click en el income con id: ${income.id}", Toast.LENGTH_SHORT).show()
        val dialog = ExpenseDialog(newExpense = false, expense = expense, updateUI = {
            updateUI()
        }, message = { text ->
            message(text)
        })
        dialog.show(childFragmentManager, "dialog")
    }


    private fun updateUI(){
//        lifecycleScope.launch {
//            val userEmail = getCurrentUserEmail()
//            expenses = repository.getAllExpenses().filter { it.userEmailExpense == userEmail }
//
//            if(expenses.isNotEmpty()){
//                // Hay por lo menos un registro
//                binding.tvSinRegistros.visibility = View.INVISIBLE
//                binding.blankExpenseMovementsIcon.visibility = View.INVISIBLE
//
//            } else {
//                // No hay registros
//                binding.tvSinRegistros.visibility = View.VISIBLE
//                binding.blankExpenseMovementsIcon.visibility = View.VISIBLE
//            }
//            expenseAdapter.updateList(expenses)
//        }
        finanzappViewModel.AllExpense.observe(viewLifecycleOwner){expenses ->
            if(expenses.isNotEmpty()){
                // Hay por lo menos un registro
                binding.tvSinRegistros.visibility = View.INVISIBLE
                binding.blankExpenseMovementsIcon.visibility = View.INVISIBLE

            } else {
                // No hay registros
                binding.tvSinRegistros.visibility = View.VISIBLE
                binding.blankExpenseMovementsIcon.visibility = View.VISIBLE
            }
            expenseAdapter.updateList(expenses)
        }

    }


    private fun message(text: String){
        Snackbar.make(binding.expenseListFrame, text, Snackbar.LENGTH_SHORT)
            .setTextColor(Color.parseColor("#FFFFFF"))
            .setBackgroundTint(Color.parseColor("#9E1734"))
            .show()
    }
    private fun getCurrentUserEmail(): String {
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        val googleSignInAccount = GoogleSignIn.getLastSignedInAccount(requireContext())

        return when {
            firebaseUser != null -> firebaseUser.email ?: ""
            googleSignInAccount != null -> googleSignInAccount.email ?: ""
            else -> {
                val sharedPreferences = activity?.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                sharedPreferences?.getString("user_email", "") ?: ""
            }
        }
    }
    companion object {
        @JvmStatic
        fun newInstance() = ExpenseMovementsFragment()
    }
}