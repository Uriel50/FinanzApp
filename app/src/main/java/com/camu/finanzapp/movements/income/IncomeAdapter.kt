package com.camu.finanzapp.movements.income

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.camu.finanzapp.database.IncomeEntity
import com.camu.finanzapp.databinding.ItemMovementIncomeBinding

class IncomeAdapter(private val onIncomeClick: (IncomeEntity) -> Unit):RecyclerView.Adapter<IncomeAdapter.ViewHolder>() {

    private var incomes: List<IncomeEntity> = emptyList()

    class ViewHolder(private val binding: ItemMovementIncomeBinding):RecyclerView.ViewHolder(binding.root){



        fun bind(income: IncomeEntity){
            binding.apply {
                tvTitleIncome.text = income.incomeName
                tvCategory.text = income.incomeCategory
                tvDateIncome.text = income.incomeDate
                tvMountIncome.text = "+$"+income.incomeMount.toString()

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMovementIncomeBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }


    override fun getItemCount():Int = incomes.size
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(incomes[position])

        holder.itemView.setOnClickListener {
            onIncomeClick(incomes[position])
        }
    }

    fun updateList(list: List<IncomeEntity>){
        incomes = list
        notifyDataSetChanged()
    }

}