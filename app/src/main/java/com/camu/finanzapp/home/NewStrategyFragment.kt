package com.camu.finanzapp.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.camu.finanzapp.R
import com.camu.finanzapp.databinding.FragmentNewStrategyBinding

class NewStrategyFragment : Fragment(R.layout.fragment_new_strategy) {

    private lateinit var binding: FragmentNewStrategyBinding



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentNewStrategyBinding.bind(view)

        val newStrategyButton = binding.buttonNewStrategy
        newStrategyButton.setOnClickListener {
            val intent = Intent(context, RegisterStrategyActivity::class.java)
            startActivity(intent)
        }


    }

    companion object {

        @JvmStatic
        fun newInstance() = NewStrategyFragment()
    }
}