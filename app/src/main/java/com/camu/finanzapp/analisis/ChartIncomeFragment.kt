package com.camu.finanzapp.analisis

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.camu.finanzapp.R
import com.camu.finanzapp.database.DataBase
import com.camu.finanzapp.database.DataBaseRepository
import com.camu.finanzapp.database.TotalsEntity
import com.camu.finanzapp.databinding.FragmentChartIncomeBinding
import com.camu.finanzapp.movements.FinanzappViewModel
import com.camu.finanzapp.movements.expense.ExpenseItem
import com.camu.finanzapp.movements.income.IncomeItem
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch

class ChartIncomeFragment : Fragment(R.layout.fragment_chart_income) {

    private lateinit var binding: FragmentChartIncomeBinding
    private lateinit var database: DataBase
    private lateinit var repository: DataBaseRepository
    private val finanzappViewModel: FinanzappViewModel by viewModels()
    private val chartIncomeCoroutineScope = CoroutineScope(Dispatchers.IO)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentChartIncomeBinding.bind(view)


        database = DataBase.getDataBase(requireContext())
        repository = DataBaseRepository(
            database.userDao(),
            database.totalDao(),
            database.reminderDao(),
            database.incomeDao(),
            database.expenseDao(),
            database.budgetDao()
        )

        finanzappViewModel.TotalByEmailLiveData.observe(viewLifecycleOwner){total->
            if (total != null && total.totalIncome!= 0.0 && total.totalExpense != 0.0){
                binding.chartIncomeView.visibility = View.VISIBLE
                binding.ivBlankChart.visibility = View.INVISIBLE
                createChart(binding.chartIncomeView)
            }else{
                binding.ivBlankChart.visibility = View.VISIBLE
                binding.chartIncomeView.visibility = View.INVISIBLE
            }
        }

    }

    fun createChart(chartIncomeView: PieChart) {

        val categoryItems = listOf(
            "Salario",
            "Inversiones",
            "Alquileres",
            "Pension",
            "Regalias",
            "Otros"
        )
        chartIncomeCoroutineScope.launch {
            val userEmail = getUserEmail()
            val incomesSalary = repository?.getIncomesByCategory(userEmail, categoryItems[0])
            val incomesInvestments = repository?.getIncomesByCategory(userEmail, categoryItems[1])
            val incomesRentals = repository?.getIncomesByCategory(userEmail, categoryItems[2])
            val incomesPension = repository?.getIncomesByCategory(userEmail, categoryItems[3])
            val incomesRoyalties = repository?.getIncomesByCategory(userEmail, categoryItems[4])
            val incomesOthers = repository?.getIncomesByCategory(userEmail, categoryItems[5])

            val listPie = mutableListOf<PieEntry>()

            incomesSalary?.let {
                listPie.add(PieEntry(it.toFloat(), categoryItems[0]))
            }
            incomesInvestments?.let {
                listPie.add(PieEntry(it.toFloat(), categoryItems[1]))
            }
            incomesRentals?.let {
                listPie.add(PieEntry(it.toFloat(), categoryItems[2]))
            }
            incomesPension?.let {
                listPie.add(PieEntry(it.toFloat(), categoryItems[3]))
            }
            incomesRoyalties?.let {
                listPie.add(PieEntry(it.toFloat(), categoryItems[4]))
            }
            incomesOthers?.let {
                listPie.add(PieEntry(it.toFloat(), categoryItems[5]))
            }

            val pieDataSet = PieDataSet(listPie, "").apply {
                setColors(ColorTemplate.MATERIAL_COLORS, 255)
                valueTextSize = 15f
                valueTextColor = Color.WHITE
            }

            val pieData = PieData(pieDataSet)

            chartIncomeView.apply {
                data = pieData
                description.text = "Categorias"
                centerText = "Ingresos"
                notifyDataSetChanged()
                invalidate()
            }
        }


    }

    override fun onDestroy() {
        chartIncomeCoroutineScope.coroutineContext.cancelChildren()
        super.onDestroy()
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
        fun newInstance() = ChartIncomeFragment()
    }
}