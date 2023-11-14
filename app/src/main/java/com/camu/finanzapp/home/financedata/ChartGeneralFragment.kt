package com.camu.finanzapp.home.financedata

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.camu.finanzapp.R
import com.camu.finanzapp.databinding.FragmentChartGeneralBinding
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate

class ChartGeneralFragment : Fragment(R.layout.fragment_chart_general) {
    private lateinit var binding: FragmentChartGeneralBinding



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentChartGeneralBinding.bind(view)
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

        binding.pcView.apply {
            data = pieData
            description.text = "Encuesta"
            centerText = "Resultados"
            animateY(1000)
        }




    }

    companion object {
        @JvmStatic
        fun newInstance() = ChartGeneralFragment()
    }
}

