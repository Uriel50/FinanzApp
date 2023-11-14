package com.camu.finanzapp.reminders

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.camu.finanzapp.R
import com.camu.finanzapp.databinding.FragmentRemindersBinding
import com.camu.finanzapp.home.NewStrategyFragment
import com.camu.finanzapp.util.GlobalData


class RemindersFragment : Fragment(R.layout.fragment_reminders) {

    private lateinit var binding : FragmentRemindersBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRemindersBinding.bind(view)

        if (!GlobalData.isReminder){
            val childFragment = RemidersListFragment()
            childFragmentManager.beginTransaction()
                .replace(R.id.child_fragment_container_reminders, childFragment)
                .commit()
        }
        else{
        }
    }


    companion object {

        @JvmStatic
        fun newInstance() = RemindersFragment()
    }
}