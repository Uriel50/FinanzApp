package com.camu.finanzapp.analisis

import ChartsAnalisisFragment
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.camu.finanzapp.R
import com.camu.finanzapp.databinding.FragmentAnalisisBinding
import com.camu.finanzapp.home.NewStrategyFragment
import com.camu.finanzapp.util.GlobalData.isBalance

class AnalisisFragment : Fragment(R.layout.fragment_analisis) {

    private lateinit var binding: FragmentAnalisisBinding


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAnalisisBinding.bind(view)


        if (isBalance){

            val childFragment = ChartsAnalisisFragment()
            childFragmentManager.beginTransaction()
                .replace(R.id.child_fragment_container, childFragment)
                .commit()
        }
        else{
            val childFragment = NewStrategyFragment()
            childFragmentManager.beginTransaction()
                .replace(R.id.child_fragment_container, childFragment)
                .commit()
        }




    }




    companion object {
        @JvmStatic
        fun newInstance() = AnalisisFragment()
    }
}


