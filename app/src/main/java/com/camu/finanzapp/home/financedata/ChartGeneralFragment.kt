package com.camu.finanzapp.home.financedata
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.camu.finanzapp.R
import com.camu.finanzapp.database.DataBase
import com.camu.finanzapp.database.DataBaseRepository
import com.camu.finanzapp.databinding.FragmentChartGeneralBinding
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.coroutines.launch

class ChartGeneralFragment : Fragment(R.layout.fragment_chart_general) {
    private lateinit var binding: FragmentChartGeneralBinding
    private lateinit var database: DataBase
    private lateinit var repository: DataBaseRepository



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentChartGeneralBinding.bind(view)

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
            val Incomes = repository.getAllIncome().filter { it.userEmailIncome == email }
            val Expenses = repository.getAllExpenses().filter { it.userEmailExpense == email }

            var totalIncomes = Incomes.sumByDouble { it.incomeMount }
            var totatExpenses = Expenses.sumByDouble { it.expenseMount }

            repository.updateTotalIncome(totalIncomes,email)
            repository.updateTotalExpense(totatExpenses,email)


            val Total = repository.getTotalByEmail(email)

            val listPie = mutableListOf<PieEntry>()
            var income:Float = 0.0F
            var expense:Float = 0.0F

            if (Total!=null){
                income = Total.totalIncome.toFloat()
                expense = Total.totalExpense.toFloat()
            }

            listPie.add(PieEntry(income, "Ingresos"))
            listPie.add(PieEntry(expense, "Gastos"))

            val pieDataSet = PieDataSet(listPie, "").apply {

                val colors = intArrayOf(
                    Color.rgb(8, 60, 164), // Azul #083ca4
                    Color.rgb(255, 0, 0)   // Rojo #FF0000
                    // Añade más colores si es necesario
                )

                setColors(colors, 255)
                valueTextSize = 15f
                valueTextColor = Color.WHITE
            }

            val pieData = PieData(pieDataSet)

            val textTotal = "Total: $"+Total?.balanceTotal.toString()

            binding.pcView.apply {
                data = pieData
                description.text = ""
                centerText = "Gastos e Ingresos"
                animateY(1000)
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
        fun newInstance() = ChartGeneralFragment()
    }
}

