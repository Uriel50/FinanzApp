package com.camu.finanzapp.analisis

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.camu.finanzapp.R
import com.camu.finanzapp.databinding.FragmentNewStrategyBinding

class NewStrategyFragment : Fragment(R.layout.fragment_new_strategy) {

    private lateinit var binding: FragmentNewStrategyBinding



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentNewStrategyBinding.bind(view)


    }

    companion object {

        @JvmStatic
        fun newInstance() = NewStrategyFragment()
    }
}