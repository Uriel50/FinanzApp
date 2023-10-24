package com.camu.finanzapp.financedata

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.camu.finanzapp.R
import com.camu.finanzapp.databinding.FragmentBalanceBinding


class BalanceFragment : Fragment(R.layout.fragment_balance) {

    private lateinit var binding: FragmentBalanceBinding


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentBalanceBinding.bind(view)

    }

    companion object {
        @JvmStatic
        fun newInstance() = BalanceFragment()
    }
}