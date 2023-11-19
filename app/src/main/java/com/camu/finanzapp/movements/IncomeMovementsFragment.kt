package com.camu.finanzapp.movements

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.camu.finanzapp.R
import com.camu.finanzapp.databinding.FragmentIncomeMovementsBinding

class IncomeMovementsFragment : Fragment(R.layout.fragment_income_movements) {

    private lateinit var binding: FragmentIncomeMovementsBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentIncomeMovementsBinding.bind(view)


    }

    companion object {
        @JvmStatic
        fun newInstance() = IncomeMovementsFragment()
    }
}