package com.camu.finanzapp.movements.expense

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.camu.finanzapp.R
import com.camu.finanzapp.database.ExpenseEntity
import com.camu.finanzapp.databinding.SpinnerItemMovementsBinding
import com.camu.finanzapp.movements.income.IncomeItem

class ExpenseCategoryAdapter (private val context: Context, private val items: List<ExpenseItem>): ArrayAdapter<ExpenseItem>(context,
    R.layout.spinner_item_movements,items){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding : SpinnerItemMovementsBinding = if (convertView == null){
            val inflater = LayoutInflater.from(context)
            val itemBinding = SpinnerItemMovementsBinding.inflate(inflater,parent,false)
            itemBinding.root.tag = itemBinding
            itemBinding
        }else{
            convertView.tag as SpinnerItemMovementsBinding
        }
        val item = items[position]
        binding.textView.text = item.text
        return binding.root
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getView(position,convertView,parent)
    }
}