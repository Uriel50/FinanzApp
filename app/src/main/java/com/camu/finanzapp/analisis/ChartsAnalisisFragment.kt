import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.camu.finanzapp.R
import com.camu.finanzapp.adapters.ViewPageIncomeExpenseAdapter
import com.camu.finanzapp.adapters.ViewPagerAdapter
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
import java.util.ArrayList

class ChartsAnalisisFragment : Fragment() {

    private lateinit var binding: FragmentChartsAnalisisBinding
    private lateinit var chart: BarChart
    private val months = arrayOf("Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio")
    private lateinit var viewPagerInEx: ViewPager
    private lateinit var tabLayoutInEX: TabLayout


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChartsAnalisisBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

        val set1 = BarDataSet(values1, "Company A").apply {
            color = requireContext().resources.getColor(R.color.buttoncolor)
        }
        val set2 = BarDataSet(values2, "Company B").apply {
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



        //***********LineBarIncome*********************

        val progressBarIncome = binding.progressBarIncome
        val progressTextIncome= binding.progressTextIncome
        val percentageTextIncome = binding.percentageTextIncome

        val total = 770
        val income = 406

        progressBarIncome.max = total
        progressBarIncome.progress = income

        // Asumiendo que gasto debe ser un Double
        progressTextIncome.text = String.format("$%,.2f", income.toDouble())

        // Calcular el porcentaje y formatear el total correctamente.
        val percentageIncome = (income.toDouble() / total.toDouble()) * 100
        percentageTextIncome.text = String.format("%.0f%% de $%,.2f previstos", percentageIncome, total.toDouble())


        //**********LineBarExpeses************
        val progressBarExpenses = binding.progressBarExpeses
        val progressTextExpenses= binding.progressTextExpenses
        val percentageTextExpenses = binding.percentageTextExpenses

        val expense = 200

        progressBarExpenses.max = total
        progressBarExpenses.progress = expense

        // Asumiendo que gasto debe ser un Double
        progressTextExpenses.text = String.format("$%,.2f", expense.toDouble())

        // Calcular el porcentaje y formatear el total correctamente.
        val percentageExpense = (expense.toDouble() / total.toDouble()) * 100
        percentageTextExpenses.text = String.format("%.0f%% de $%,.2f previstos", percentageExpense, total.toDouble())



        //********ChartIncome and Expense**************************


        viewPagerInEx = binding.viewPagerIncomeExpense
        tabLayoutInEX = binding.tabLayoutIncomeExpense

        val adapterInEx = ViewPageIncomeExpenseAdapter(childFragmentManager)
        viewPagerInEx.adapter = adapterInEx

        tabLayoutInEX.setupWithViewPager(viewPagerInEx)

    }


    companion object {
        @JvmStatic
        fun newInstance() = ChartsAnalisisFragment()
    }
}
