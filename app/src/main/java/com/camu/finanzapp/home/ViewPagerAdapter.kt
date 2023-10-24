package com.camu.finanzapp.home

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.camu.finanzapp.analisis.AnalisisFragment

// Adaptador de vista para gestionar los fragmentos en un ViewPager
class ViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    // Devuelve el fragmento correspondiente según la posición
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> HomeFragment()
            1 -> AnalisisFragment()
            2 -> BudgetFragment()
            3 -> RemindersFragment()
            else -> throw IllegalArgumentException("Invalid position: $position")
        }
    }

    // Devuelve la cantidad total de fragmentos en el ViewPager
    override fun getCount(): Int {
        return 4
    }
}
