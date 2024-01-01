package com.camu.finanzapp.movements.income

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.camu.finanzapp.R
import com.camu.finanzapp.database.DataBase
import com.camu.finanzapp.database.DataBaseRepository
import com.camu.finanzapp.database.IncomeEntity
import com.camu.finanzapp.databinding.FragmentIncomeMovementsBinding
import com.camu.finanzapp.home.RegisterStrategyActivity
import com.camu.finanzapp.movements.FinanzappViewModel
import com.camu.finanzapp.movements.expense.NewIncomeActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class IncomeMovementsFragment : Fragment(R.layout.fragment_income_movements) {

    private lateinit var binding: FragmentIncomeMovementsBinding
    private lateinit var database: DataBase
    private lateinit var repository: DataBaseRepository
    private var incomes: List<IncomeEntity> = emptyList()
    private val finanzappViewModel: FinanzappViewModel by viewModels()

    private lateinit var incomeAdapter: IncomeAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentIncomeMovementsBinding.bind(view)


        database = DataBase.getDataBase(requireContext())
        repository = DataBaseRepository(
            database.userDao(),
            database.totalDao(),
            database.reminderDao(),
            database.incomeDao(),
            database.expenseDao(),
            database.budgetDao()
        )

        incomeAdapter = IncomeAdapter (){income ->
            incomeClicked(income)

        }

        binding.listIncomeMovements.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = incomeAdapter
        }

        updateUI()

        binding.buttonNewIncomeMovements.setOnClickListener {

            val dialog = IncomeDialog(updateUI = {
                updateUI()
            }, message = {text ->
                message(text)
            })
            dialog.show(parentFragmentManager,"dialog")
        }

    }



    private fun incomeClicked(income: IncomeEntity){
        //Toast.makeText(this, "Click en el income con id: ${income.id}", Toast.LENGTH_SHORT).show()
        val dialog = IncomeDialog(newIncome = false, income = income, updateUI = {
            updateUI()
        }, message = { text ->
            message(text)
        })
        dialog.show(childFragmentManager, "dialog")
    }


    private fun updateUI(){
//        lifecycleScope.launch {
//            val userEmail = getCurrentUserEmail()
////            incomes = repository.getAllIncome().filter { it.userEmailIncome == userEmail }
//
//            if(incomes.isNotEmpty()){
//                // Hay por lo menos un registro
//                binding.tvSinRegistros.visibility = View.INVISIBLE
//                binding.blankIncomeMovementsIcon.visibility = View.INVISIBLE
//
//            } else {
//                // No hay registros
//                binding.tvSinRegistros.visibility = View.VISIBLE
//                binding.blankIncomeMovementsIcon.visibility = View.VISIBLE
//            }
//            incomeAdapter.updateList(incomes)
//        }
        finanzappViewModel.AllIncomes.observe(viewLifecycleOwner) { incomes ->
            if (incomes.isNotEmpty()) {
                binding.tvSinRegistros.visibility = View.INVISIBLE
                binding.blankIncomeMovementsIcon.visibility = View.INVISIBLE
            } else {
                binding.tvSinRegistros.visibility = View.VISIBLE
                binding.blankIncomeMovementsIcon.visibility = View.VISIBLE
            }
            incomeAdapter.updateList(incomes)
        }

    }

    private fun message(text: String){
        Snackbar.make(binding.cl, text, Snackbar.LENGTH_SHORT)
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
        fun newInstance() = IncomeMovementsFragment()
    }
}