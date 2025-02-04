package com.example.reto10

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyAdapter(val context: Context, dataList: List<MyDataItem>) : RecyclerView.Adapter<MyAdapter.ViewHolder>() {

    private var dataList: List<MyDataItem> = dataList  // Lista mutable
    private val originalDataList: List<MyDataItem> = dataList.toList() // Copia inmutable


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var empresa_id_text: TextView
        var empresa_nombre_text: TextView
        var empresa_url_text: TextView
        var empresa_telefono_text: TextView

        init {
            empresa_id_text = itemView.findViewById(R.id.empresa_id_text)
            empresa_nombre_text = itemView.findViewById(R.id.empresa_nombre_text)
            empresa_url_text = itemView.findViewById(R.id.empresa_url_text)
            empresa_telefono_text = itemView.findViewById(R.id.empresa_telefono_text)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var itemView = LayoutInflater.from(context).inflate(R.layout.row_item, parent, false)
        return  ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return dataList.size // Usa dataList aquí
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataList[position] // Obtén el item actual de dataList
        holder.empresa_id_text.text = item.calendario?.toString() ?: "" // Manejo de nulos con Elvis operator
        holder.empresa_nombre_text.text = item.nombreestablecimiento?.toString() ?: ""
        holder.empresa_url_text.text = item.direccion?.toString() ?: ""
        holder.empresa_telefono_text.text = item.telefono?.toString() ?: ""
    }

    fun updateData(newDataList: List<MyDataItem>) {
        this.dataList = newDataList
        notifyDataSetChanged()
    }
}