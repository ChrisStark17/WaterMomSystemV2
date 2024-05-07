package com.example.wabu.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.wabu.HistorialRecord
import com.example.wabu.databinding.ItemHistorialBinding

class HistorialAdp : RecyclerView.Adapter<HistorialAdp.ViewHolder>() {

    private var recordList: List<HistorialRecord> = listOf()

    var onClickRecord: ((HistorialRecord) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemHistorialBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val record = recordList[position]
        holder.bind(record, onClickRecord)
    }

    override fun getItemCount(): Int = recordList.size

    class ViewHolder(private val binding: ItemHistorialBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(record: HistorialRecord, onClickRecord: ((HistorialRecord) -> Unit)?) {
            binding.litrosTextView.text = "Litros: ${record.Litros}"
            binding.txtDate.text = "Fecha y hora: ${record.FechaHora}"
            binding.cvContainer.setOnClickListener { onClickRecord?.invoke(record) }
        }
    }

    fun setList(recordList: List<HistorialRecord>?) {
        if (!recordList.isNullOrEmpty()) {
            this.recordList = recordList
            notifyDataSetChanged()
        }
    }

}