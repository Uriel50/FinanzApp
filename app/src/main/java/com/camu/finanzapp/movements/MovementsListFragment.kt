package com.camu.finanzapp.movements

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.viewpager.widget.ViewPager
import com.camu.finanzapp.R
import com.camu.finanzapp.adapters.ViewPageIncomeExpenseAdapter
import com.camu.finanzapp.adapters.ViewPageMovementsAdapter
import com.camu.finanzapp.database.DataBase
import com.camu.finanzapp.database.DataBaseRepository
import com.camu.finanzapp.databinding.FragmentChartsAnalisisBinding
import com.camu.finanzapp.databinding.FragmentMovementsListBinding
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.launch


class MovementsListFragment : Fragment(R.layout.fragment_movements_list) {

    private lateinit var binding: FragmentMovementsListBinding
    private lateinit var database: DataBase
    private lateinit var repository: DataBaseRepository
    private lateinit var viewPager: ViewPager
    private lateinit var tabLayout: TabLayout




    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMovementsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
            var Budget = repository.getBudgetByEmail(email)
            var Total = repository.getTotalByEmail(email)


            binding.nameBudget.text = Budget?.nameBudget
            binding.mountBalanceTotal.text = "$"+Total?.balanceTotal.toString()

        }

        viewPager = binding.viewPagerIncomeExpenseMovements
        tabLayout = binding.tabLayoutMovements

        val adapterInEx = ViewPageMovementsAdapter(childFragmentManager)
        viewPager.adapter = adapterInEx

        tabLayout.setupWithViewPager(viewPager)

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
        fun newInstance() = MovementsListFragment()
    }
}