package com.camu.finanzapp.home.financedata

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.camu.finanzapp.R
import com.camu.finanzapp.database.DataBase
import com.camu.finanzapp.database.DataBaseRepository
import com.camu.finanzapp.database.TotalsEntity
import com.camu.finanzapp.databinding.FragmentBalanceBinding
import kotlinx.coroutines.launch


class BalanceFragment : Fragment(R.layout.fragment_balance) {

    private lateinit var binding: FragmentBalanceBinding
    private lateinit var database: DataBase
    private lateinit var repository: DataBaseRepository
    private var Total : TotalsEntity? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentBalanceBinding.bind(view)

        database = DataBase.getDataBase(requireContext())
        repository = DataBaseRepository(
            database.userDao(),
            database.totalDao(),
            database.reminderDao(),
            database.incomeDao(),
            database.expenseDao(),
            database.budgetDao()
        )

        lifecycleScope.launch {
            val email = getUserEmail()
            Total = repository.getTotalByEmail(email)
            binding.mountBalanceIncome.text = "$"+Total?.totalIncome.toString()
            binding.mountBalanceExpense.text = "-$"+Total?.totalExpense.toString()
            binding.mountBalanceTotal.text = "$"+Total?.balanceTotal.toString()
        }
    }

    private fun getUserEmail(): String {
        val sharedPrefKey = "user_email"

        // Obtener el contexto de la Activity asociada
        val sharedPreferences = activity?.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        // Utilizar el contexto para acceder a SharedPreferences, si está disponible
        return sharedPreferences?.getString(sharedPrefKey, "") ?: ""
    }

    companion object {
        @JvmStatic
        fun newInstance() = BalanceFragment()
    }
}