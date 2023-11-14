package com.camu.finanzapp.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.camu.finanzapp.analisis.ChartExpenseFragment
import com.camu.finanzapp.analisis.ChartIncomeFragment

class ViewPageIncomeExpenseAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> ChartIncomeFragment()
            1 -> ChartExpenseFragment()
            else -> Fragment()
        }
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "Ingresos"
            1 -> "Gastos"
            else -> null
        }
    }
}
