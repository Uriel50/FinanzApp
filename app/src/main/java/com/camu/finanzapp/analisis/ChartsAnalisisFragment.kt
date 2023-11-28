import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.camu.finanzapp.R
import com.camu.finanzapp.adapters.ViewPageIncomeExpenseAdapter
import com.camu.finanzapp.adapters.ViewPagerAdapter
import com.camu.finanzapp.database.DataBase
import com.camu.finanzapp.database.DataBaseRepository
import com.camu.finanzapp.database.TotalsEntity
import com.camu.finanzapp.databinding.FragmentChartsAnalisisBinding
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.launch
import java.util.ArrayList

class ChartsAnalisisFragment : Fragment() {

    private lateinit var binding: FragmentChartsAnalisisBinding
    private lateinit var chart: BarChart
    private val months = arrayOf("Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio")
    private lateinit var viewPagerInEx: ViewPager
    private lateinit var tabLayoutInEX: TabLayout
    private lateinit var database: DataBase
    private lateinit var repository: DataBaseRepository
    private var Total : TotalsEntity? = null
    private var isChartGeneral: TotalsEntity? = null




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChartsAnalisisBinding.inflate(inflater, container, false)
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

        chart = binding.pcView
        chart.description.isEnabled = false
        chart.setPinchZoom(false)
        chart.setDrawBarShadow(false)
        chart.setDrawGridBackground(false)

        val l = chart.legend
        l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        l.orientation = Legend.LegendOrientation.VERTICAL
        l.setDrawInside(false)
        l.formSize = 8f

        val xAxis = chart.xAxis
        xAxis.granularity = 1f
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setCenterAxisLabels(true)
        xAxis.valueFormatter = IndexAxisValueFormatter(months)
        xAxis.axisMinimum = 0f // Asegúrate de que el eje X comience desde 0
        xAxis.axisMaximum = months.size.toFloat() // Asegúrate de que el eje X termine en el tamaño del array de meses

        val leftAxis = chart.axisLeft
        leftAxis.setDrawGridLines(false)
        leftAxis.spaceTop = 15f
        leftAxis.axisMinimum = 0f
        chart.axisRight.isEnabled = false

        val values1 = ArrayList<BarEntry>()
        val values2 = ArrayList<BarEntry>()

        for (i in months.indices) {
            values1.add(BarEntry(i.toFloat(), (Math.random() * 10).toFloat()))
            values2.add(BarEntry(i.toFloat(), (Math.random() * 10).toFloat()))
        }

        val set1 = BarDataSet(values1, "Income").apply {
            color = requireContext().resources.getColor(R.color.buttoncolor)
        }
        val set2 = BarDataSet(values2, "Expense").apply {
            color = requireContext().resources.getColor(R.color.alert)
        }
        chart.legend.isEnabled = false

        val data = BarData(set1, set2)
        val groupSpace = 0.4f // Espacio entre grupos de barras
        val barSpace = 0.06f // Espacio entre barras dentro de un grupo
        val barWidth = (1 - groupSpace) / 2 - barSpace
        data.barWidth = barWidth

        chart.data = data
        chart.groupBars(0f, groupSpace, barSpace)
        chart.invalidate()



        //***********LineBar*********************
        lifecycleScope.launch {
            val email = getUserEmail()
            isChartGeneral = repository.getTotalByEmail(email)

            if (isChartGeneral?.totalIncome != 0.0 && isChartGeneral?.totalExpense!=0.0 && isChartGeneral!=null){
                Total = repository.getTotalByEmail(email)
                setBarLineIncome(Total)
                setBarLineExpnses(Total)

            }else{
                setBarLineExpnsesBlank()
                setBarLineIncomeBlank()
            }

        }



        //********ChartIncome and Expense**************************


        viewPagerInEx = binding.viewPagerIncomeExpense
        tabLayoutInEX = binding.tabLayoutIncomeExpense

        val adapterInEx = ViewPageIncomeExpenseAdapter(childFragmentManager)
        viewPagerInEx.adapter = adapterInEx

        tabLayoutInEX.setupWithViewPager(viewPagerInEx)

    }


    private fun getUserEmail(): String {
        val sharedPrefKey = "user_email"

        // Obtener el contexto de la Activity asociada
        val sharedPreferences = activity?.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        // Utilizar el contexto para acceder a SharedPreferences, si está disponible
        return sharedPreferences?.getString(sharedPrefKey, "") ?: ""
    }

    private fun setBarLineIncome(Total: TotalsEntity?){

        var total = 0
        var income = 0
        if (Total!=null){
            total = Total.balanceTotal.toInt()
            income = Total.totalIncome.toInt()
        }

        binding.progressBarIncome.max = total
        binding.progressBarIncome.progress = income

        // Asumiendo que gasto debe ser un Double
        binding.progressTextIncome.text = String.format("$%,.2f", income.toDouble())

        // Calcular el porcentaje y formatear el total correctamente.
        val percentageIncome = (income.toDouble() / total.toDouble()) * 100
        binding.percentageTextIncome.text = String.format("%.0f%% de $%,.2f previstos", percentageIncome, total.toDouble())
    }

    private fun setBarLineExpnses(Total: TotalsEntity?){

        var total = 0
        var expense = 0
        if (Total!=null){
            total = Total.balanceTotal.toInt()
            expense = Total.totalExpense.toInt()
        }


        binding.progressBarExpeses.max = total
        binding.progressBarExpeses.progress = expense

        // Asumiendo que gasto debe ser un Double
        binding.progressTextExpenses.text = String.format("$%,.2f", expense.toDouble())

        // Calcular el porcentaje y formatear el total correctamente.
        val percentageExpense = (expense.toDouble() / total.toDouble()) * 100
        binding.percentageTextExpenses.text = String.format("%.0f%% de $%,.2f previstos", percentageExpense, total.toDouble())

    }

    private fun setBarLineIncomeBlank(){

        var total = 0
        var income = 0

        binding.progressBarIncome.max = total
        binding.progressBarIncome.progress = income

        // Asumiendo que gasto debe ser un Double
        binding.progressTextIncome.text = String.format("$%,.2f", income.toDouble())

        // Calcular el porcentaje y formatear el total correctamente.
        val percentageIncome = (income.toDouble() / total.toDouble()) * 100
        binding.percentageTextIncome.text = String.format("%.0f%% de $%,.2f previstos", percentageIncome, total.toDouble())
    }

    private fun setBarLineExpnsesBlank(){

        var total = 0
        var expense = 0

        binding.progressBarExpeses.max = total
        binding.progressBarExpeses.progress = expense

        // Asumiendo que gasto debe ser un Double
        binding.progressTextExpenses.text = String.format("$%,.2f", expense.toDouble())

        // Calcular el porcentaje y formatear el total correctamente.
        val percentageExpense = (expense.toDouble() / total.toDouble()) * 100
        binding.percentageTextExpenses.text = String.format("%.0f%% de $%,.2f previstos", percentageExpense, total.toDouble())

    }


    companion object {
        @JvmStatic
        fun newInstance() = ChartsAnalisisFragment()
    }
}
