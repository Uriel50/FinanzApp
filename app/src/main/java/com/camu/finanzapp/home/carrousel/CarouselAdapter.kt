package com.camu.finanzapp.home.carrousel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.camu.finanzapp.R

// Adaptador para el RecyclerView del carrusel
class CarouselAdapter(private val items: List<CarouselData>) : RecyclerView.Adapter<CarouselAdapter.CarouselViewHolder>() {

    // Crea una nueva instancia de ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarouselViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.carousel_item, parent, false)
        return CarouselViewHolder(view)
    }

    // Obtiene la cantidad de elementos en la lista de datos
    override fun getItemCount(): Int {
        return Int.MAX_VALUE
    }

    // Vincula los datos a un ViewHolder específico en una posición determinada
    override fun onBindViewHolder(holder: CarouselViewHolder, position: Int) {
        val realPosition = position % items.size
        val item = items[realPosition]
        holder.bind(item)
    }

    // Clase interna para mantener la referencia a los elementos de la vista en cada ViewHolder
    inner class CarouselViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val itemText: TextView = itemView.findViewById(R.id.textAdvice)

        // Vincula los datos del modelo a los elementos de la vista
        fun bind(dataModel: CarouselData) {
            itemText.text = dataModel.textAdvice
        }
    }
}
