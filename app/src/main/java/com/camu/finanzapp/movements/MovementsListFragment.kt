package com.camu.finanzapp.movements

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.camu.finanzapp.R
import com.camu.finanzapp.databinding.FragmentMovementsListBinding


class MovementsListFragment : Fragment(R.layout.fragment_movements_list) {

    private lateinit var binding: FragmentMovementsListBinding





    companion object {
        @JvmStatic
        fun newInstance() = MovementsListFragment()
    }
}