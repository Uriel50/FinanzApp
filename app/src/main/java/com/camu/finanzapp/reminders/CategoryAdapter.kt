package com.camu.finanzapp.reminders

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.camu.finanzapp.R
import com.camu.finanzapp.databinding.SpinnerItemLayoutBinding
import com.camu.finanzapp.util.ReminderItem

class CategoryAdapter (private val context: Context, private val items: List<ReminderItem>): ArrayAdapter<ReminderItem>(context,
    R.layout.spinner_item_layout,items){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding: SpinnerItemLayoutBinding = if (convertView == null) {
            val inflater = LayoutInflater.from(context)
            val itemBinding = SpinnerItemLayoutBinding.inflate(inflater, parent, false)
            itemBinding.root.tag = itemBinding
            itemBinding
        } else {
            convertView.tag as SpinnerItemLayoutBinding
        }

        val item = items[position]
        binding.textView.text = item.text
        binding.imageView.setImageResource(item.imageResId)

        return binding.root
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getView(position, convertView, parent)
    }
}