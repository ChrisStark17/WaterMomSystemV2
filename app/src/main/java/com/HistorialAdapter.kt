package com

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.wabu.MainActivity
import com.example.wabu.R

class HistorialAdapter(private val historialDatos: List<MainActivity.HistorialItem>) :
    RecyclerView.Adapter<HistorialAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_historial, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val historialItem = historialDatos[position]
        holder.bind(historialItem)
    }

    override fun getItemCount(): Int {
        return historialDatos.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val flujoTextView: TextView = itemView.findViewById(R.id.flujoTextView)
        private val litrosTextView: TextView = itemView.findViewById(R.id.litrosTextView)
        private val porcentajeTextView: TextView = itemView.findViewById(R.id.porcentajeTextView)

        fun bind(historialItem: MainActivity.HistorialItem) {
            flujoTextView.text = historialItem.flujo.toString()
            litrosTextView.text = historialItem.litros.toString()
            porcentajeTextView.text = historialItem.porcentajeLlenado.toString()
        }
    }
}

