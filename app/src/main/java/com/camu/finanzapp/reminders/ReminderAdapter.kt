package com.camu.finanzapp.reminders

import android.app.Activity
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.camu.finanzapp.R
import com.camu.finanzapp.databasereminders.data.db.model.ReminderEntity
import com.camu.finanzapp.databinding.ReminderItemBinding
import com.camu.finanzapp.util.ReminderItem
import kotlin.coroutines.coroutineContext


class ReminderAdapter(private val onReminderClick: (ReminderEntity) -> Unit):RecyclerView.Adapter<ReminderAdapter.ViewHolder>() {

    private var reminders: List<ReminderEntity> = emptyList()

    class ViewHolder(private val binding: ReminderItemBinding):RecyclerView.ViewHolder(binding.root){
        val ivIcon = binding.ivIcon
        val constraintBackground = binding.constraintBackground

        val categoryItems = listOf(
            ReminderItem("Renta/Hipoteca", R.mipmap.ic_reminder_rent_foreground),
            ReminderItem("Luz/Electricidad", R.mipmap.ic_reminder_electricity_foreground),
            ReminderItem("Agua", R.mipmap.ic_reminder_water_foreground),
            ReminderItem("Internet/telefono", R.mipmap.ic_reminder_wifi_foreground),
            ReminderItem("Tv/Stream", R.mipmap.ic_reminder_tv_foreground),
            ReminderItem("Automotriz", R.mipmap.ic_reminder_car_foreground),
            ReminderItem("Tarjeta de credito", R.mipmap.ic_reminder_credit_card_foreground),
            ReminderItem("Otro", R.mipmap.ic_reminder_other_foreground)
        )

        fun bind(reminder:ReminderEntity){
            binding.apply {
                tvTitle.text = reminder.title
                tvCategory.text = reminder.category
                tvDate.text = reminder.date
                tvMount.text = "$"+reminder.mount

                val foundReminderItem = categoryItems.find { it.text == reminder.category }

                if(foundReminderItem != null){
                    val iconResourceId = foundReminderItem.imageResId

                    ivIcon.setImageResource(iconResourceId)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ReminderItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = reminders.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(reminders[position])

        holder.itemView.setOnClickListener{
            onReminderClick(reminders[position])
        }

        holder.ivIcon.setOnClickListener{

        }
    }

    fun updateList(list: List<ReminderEntity>){
        reminders = list
        notifyDataSetChanged()
    }
}