package com.camu.finanzapp.movements.expense

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.camu.finanzapp.database.ExpenseEntity
import com.camu.finanzapp.databinding.ItemMovementExpenseBinding

class ExpenseAdapter (private val onExpenseClick: (ExpenseEntity)-> Unit):RecyclerView.Adapter<ExpenseAdapter.ViewHolder>(){

    private var expenses: List<ExpenseEntity> = emptyList()

    class ViewHolder(private val binding: ItemMovementExpenseBinding): RecyclerView.ViewHolder(binding.root){


        fun bind(expense: ExpenseEntity){
            binding.apply {
                tvTitleExpense.text = expense.expenseName
                tvCategoryExpense.text = expense.expenseCategory
                tvDateExpense.text = expense.expenseDate
                tvMountExpense.text = "-$"+expense.expenseMount.toString()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMovementExpenseBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = expenses.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(expenses[position])

        holder.itemView.setOnClickListener {
            onExpenseClick(expenses[position])
        }
    }

    fun updateList(list: List<ExpenseEntity>){
        expenses = list
        notifyDataSetChanged()
    }

}