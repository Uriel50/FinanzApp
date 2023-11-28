package com.camu.finanzapp.movements

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.camu.finanzapp.R
import com.camu.finanzapp.database.BudgetEntity
import com.camu.finanzapp.database.DataBase
import com.camu.finanzapp.database.DataBaseRepository
import com.camu.finanzapp.databinding.FragmentMovementsBinding
import com.camu.finanzapp.home.NewStrategyFragment
import kotlinx.coroutines.launch

class MovementsFragment : Fragment(R.layout.fragment_movements) {

    private lateinit var binding: FragmentMovementsBinding
    private lateinit var database: DataBase
    private lateinit var repository: DataBaseRepository
    private var isStrategy: BudgetEntity?=null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMovementsBinding.bind(view)

        database = DataBase.getDataBase(requireContext())
        repository = DataBaseRepository(
            database.userDao(),
            database.totalDao(),
            database.reminderDao(),
            database.incomeDao(),
            database.expenseDao(),
            database.budgetDao()
        )

        lifecycleScope.launch{

            var email = getUserEmail()
            isStrategy = repository.getBudgetByEmail(email)
            if (isStrategy != null){

                val childFragment = MovementsListFragment()
                childFragmentManager.beginTransaction()
                    .replace(R.id.child_fragment_container_movements, childFragment)
                    .commit()
            }
            else{
                val childFragment = NewStrategyFragment()
                childFragmentManager.beginTransaction()
                    .replace(R.id.child_fragment_container_movements, childFragment)
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
        fun newInstance() = MovementsFragment()
    }
}