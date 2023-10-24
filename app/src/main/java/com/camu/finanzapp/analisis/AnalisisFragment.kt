package com.camu.finanzapp.analisis

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.camu.finanzapp.R
import com.camu.finanzapp.databinding.FragmentAnalisisBinding

class AnalisisFragment : Fragment(R.layout.fragment_analisis) {

    private lateinit var binding: FragmentAnalisisBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAnalisisBinding.bind(view)

        val childFragment = NewStrategyFragment()
        childFragmentManager.beginTransaction()
            .replace(R.id.child_fragment_container, childFragment)
            .commit()


    }




    companion object {
        @JvmStatic
        fun newInstance() = AnalisisFragment()
    }
}