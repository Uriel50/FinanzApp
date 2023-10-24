package com.camu.finanzapp.login

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.camu.finanzapp.R

// Adaptador para el RecyclerView que muestra una lista de datos
class DataAdapter(private var list: List<Data>) : RecyclerView.Adapter<DataViewHolder>() {

    // Crea una nueva instancia de DataViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        return DataViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.itemdata, parent, false))
    }

    // Devuelve la cantidad de elementos en la lista
    override fun getItemCount(): Int = list.size

    // Vincula los datos a un ViewHolder específico en una posición determinada
    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.render(list[position])
    }
}

// Clase para mantener la referencia a los elementos de la vista en cada ViewHolder
class DataViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val textData = view.findViewById<TextView>(R.id.tvData)

    // Vincula los datos del modelo a los elementos de la vista
    fun render(data: Data) {
        textData.text = data.data
    }
}
