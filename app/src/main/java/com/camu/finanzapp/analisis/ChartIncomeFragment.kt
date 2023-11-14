package com.camu.finanzapp.analisis

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.camu.finanzapp.R
import com.camu.finanzapp.databinding.FragmentChartIncomeBinding
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate

class ChartIncomeFragment : Fragment(R.layout.fragment_chart_income) {

    private lateinit var binding: FragmentChartIncomeBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentChartIncomeBinding.bind(view)

        val listPie = mutableListOf<PieEntry>()

        listPie.add(PieEntry(200f, "Comida"))
        listPie.add(PieEntry(100f, "Entretenimiento"))
        listPie.add(PieEntry(52f, "Cine"))
        listPie.add(PieEntry(88f, "Comunicaciones"))

        val pieDataSet = PieDataSet(listPie, "").apply {
            setColors(ColorTemplate.MATERIAL_COLORS, 255)
            valueTextSize = 15f
            valueTextColor = Color.WHITE
        }

        val pieData = PieData(pieDataSet)

        binding.chartIncomeView.apply {
            data = pieData
            description.text = "Encuesta"
            centerText = "Resultados"
            animateY(1000)
        }

    }

    companion object {

        @JvmStatic
        fun newInstance() = ChartIncomeFragment()
    }
}