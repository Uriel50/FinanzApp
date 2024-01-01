package com.camu.finanzapp.analisis

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.camu.finanzapp.R
import com.camu.finanzapp.database.DataBase
import com.camu.finanzapp.database.DataBaseRepository
import com.camu.finanzapp.database.TotalsEntity
import com.camu.finanzapp.databinding.FragmentChartExpenseBinding
import com.camu.finanzapp.movements.FinanzappViewModel
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch


class ChartExpenseFragment : Fragment(R.layout.fragment_chart_expense) {

    private lateinit var binding: FragmentChartExpenseBinding
    private lateinit var database: DataBase
    private lateinit var repository: DataBaseRepository
    private val finanzappViewModel: FinanzappViewModel by viewModels()
    private val chartExpenseCoroutineScope = CoroutineScope(Dispatchers.IO)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentChartExpenseBinding.bind(view)


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
                binding.chartExpenseView.visibility = View.VISIBLE
                binding.ivBlankChart.visibility = View.INVISIBLE
                createChart(binding.chartExpenseView)
            }else{
                binding.ivBlankChart.visibility = View.VISIBLE
                binding.chartExpenseView.visibility = View.INVISIBLE

            }
        }





    }

    fun createChart(chartIncomeView: PieChart) {

        val categoryItems = listOf(
            "Comida",
            "Transporte",
            "Vivienda",
            "Entretenimiento",
            "Salud",
            "Educación",
            "Impuestos",
            "Viajes",
            "Otros"
        )

        chartExpenseCoroutineScope.launch {
            val userEmail = getUserEmail()

            val expenseFood = repository?.getExpensesByCategory(userEmail,categoryItems[0])
            val expenseTrans = repository?.getExpensesByCategory(userEmail,categoryItems[1])
            val expenseHome = repository?.getExpensesByCategory(userEmail,categoryItems[2])
            val expenseEntre = repository?.getExpensesByCategory(userEmail,categoryItems[3])
            val expenseHealth = repository?.getExpensesByCategory(userEmail,categoryItems[4])
            val expenseEducation = repository?.getExpensesByCategory(userEmail,categoryItems[5])
            val expenseTaxes= repository?.getExpensesByCategory(userEmail,categoryItems[6])
            val expenseTravels = repository?.getExpensesByCategory(userEmail,categoryItems[7])
            val expenseOthers = repository?.getExpensesByCategory(userEmail,categoryItems[8])


            val listPie = mutableListOf<PieEntry>()

            expenseFood?.let {
                listPie.add(PieEntry(it.toFloat(), categoryItems[0]))
            }
            expenseTrans?.let {
                listPie.add(PieEntry(it.toFloat(), categoryItems[1]))
            }
            expenseHome?.let {
                listPie.add(PieEntry(it.toFloat(), categoryItems[2]))
            }
            expenseEntre?.let {
                listPie.add(PieEntry(it.toFloat(), categoryItems[3]))
            }
            expenseHealth?.let {
                listPie.add(PieEntry(it.toFloat(), categoryItems[4]))
            }
            expenseEducation?.let {
                listPie.add(PieEntry(it.toFloat(), categoryItems[5]))
            }
            expenseTaxes?.let {
                listPie.add(PieEntry(it.toFloat(), categoryItems[6]))
            }
            expenseTravels?.let {
                listPie.add(PieEntry(it.toFloat(), categoryItems[7]))
            }
            expenseOthers?.let {
                listPie.add(PieEntry(it.toFloat(), categoryItems[8]))
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
                centerText = "Gastos"
                notifyDataSetChanged()
                invalidate()

            }

        }

    }

    override fun onDestroy() {
        chartExpenseCoroutineScope.coroutineContext.cancelChildren()
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
        fun newInstance() = ChartExpenseFragment()
    }
}