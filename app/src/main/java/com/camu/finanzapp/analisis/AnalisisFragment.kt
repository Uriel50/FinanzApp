package com.camu.finanzapp.analisis

import ChartsAnalisisFragment
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.camu.finanzapp.R
import com.camu.finanzapp.database.BudgetEntity
import com.camu.finanzapp.database.DataBase
import com.camu.finanzapp.database.DataBaseRepository
import com.camu.finanzapp.database.ReminderEntity
import com.camu.finanzapp.databinding.FragmentAnalisisBinding
import com.camu.finanzapp.home.NewStrategyFragment
import com.camu.finanzapp.util.GlobalData
import kotlinx.coroutines.launch

class AnalisisFragment : Fragment(R.layout.fragment_analisis) {

    private lateinit var binding: FragmentAnalisisBinding
    private lateinit var database: DataBase
    private lateinit var repository: DataBaseRepository
    private var isStrategy: BudgetEntity?=null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAnalisisBinding.bind(view)

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
            var email = getUserEmail()
            isStrategy = repository.getBudgetByEmail(email)


            if (isStrategy != null){

                val childFragment = ChartsAnalisisFragment()
                childFragmentManager.beginTransaction()
                    .replace(R.id.child_fragment_container, childFragment)
                    .commit()
            }
            else{
                val childFragment = NewStrategyFragment()
                childFragmentManager.beginTransaction()
                    .replace(R.id.child_fragment_container, childFragment)
                    .commit()
            }
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
        fun newInstance() = AnalisisFragment()
    }
}


