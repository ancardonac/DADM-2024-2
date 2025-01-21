package com.example.reto8

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CustomAdapter(
    private val activity: Activity,
    private val context: Context,
    private val empresaID: MutableList<String>,
    private val empresaNombre: MutableList<String>,
    private val empresaUrl: MutableList<String>,
    private val empresaTelefono: MutableList<String>,
    private val empresaMail: MutableList<String>,
    private val empresaProducto: MutableList<String>,
    private val empresaClase: MutableList<String>
) : RecyclerView.Adapter<CustomAdapter.MyViewHolder>() {

    // Listas para filtrar
    private val filteredEmpresaID = empresaID.toMutableList()
    private val filteredEmpresaNombre = empresaNombre.toMutableList()
    private val filteredEmpresaUrl = empresaUrl.toMutableList()
    private val filteredEmpresaTelefono = empresaTelefono.toMutableList()
    private val filteredEmpresaMail = empresaMail.toMutableList()
    private val filteredEmpresaProducto = empresaProducto.toMutableList()
    private val filteredEmpresaClase = empresaClase.toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.my_row, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.empresa_id_text.text = filteredEmpresaID[position]
        holder.empresa_nombre_text.text = filteredEmpresaNombre[position]
        holder.empresa_url_text.text = filteredEmpresaUrl[position]
        holder.empresa_telefono_text.text = filteredEmpresaTelefono[position]

        // RecyclerView onClickListener
        holder.mainLayout.setOnClickListener {
            val intent = Intent(context, UpdateActivity::class.java)
            intent.putExtra("id", filteredEmpresaID[position])
            intent.putExtra("nombre", filteredEmpresaNombre[position])
            intent.putExtra("url", filteredEmpresaUrl[position])
            intent.putExtra("telefono", filteredEmpresaTelefono[position])
            intent.putExtra("mail", filteredEmpresaMail[position])
            intent.putExtra("producto", filteredEmpresaProducto[position])
            intent.putExtra("clase", filteredEmpresaClase[position])
            activity.startActivityForResult(intent, 1)
        }
    }

    override fun getItemCount(): Int {
        return filteredEmpresaID.size
    }

    // Método para filtrar por nombre
    fun filter(query: String) {
        val lowerCaseQuery = query.lowercase()

        filteredEmpresaID.clear()
        filteredEmpresaNombre.clear()
        filteredEmpresaUrl.clear()
        filteredEmpresaTelefono.clear()
        filteredEmpresaMail.clear()
        filteredEmpresaProducto.clear()
        filteredEmpresaClase.clear()

        if (query.isEmpty()) {
            // Si no hay filtro, restaurar todos los datos
            filteredEmpresaID.addAll(empresaID)
            filteredEmpresaNombre.addAll(empresaNombre)
            filteredEmpresaUrl.addAll(empresaUrl)
            filteredEmpresaTelefono.addAll(empresaTelefono)
            filteredEmpresaMail.addAll(empresaMail)
            filteredEmpresaProducto.addAll(empresaProducto)
            filteredEmpresaClase.addAll(empresaClase)
        } else {
            // Filtrar por coincidencia en el nombre
            for (i in empresaNombre.indices) {
                if (empresaNombre[i].lowercase().contains(lowerCaseQuery)) {
                    filteredEmpresaID.add(empresaID[i])
                    filteredEmpresaNombre.add(empresaNombre[i])
                    filteredEmpresaUrl.add(empresaUrl[i])
                    filteredEmpresaTelefono.add(empresaTelefono[i])
                    filteredEmpresaMail.add(empresaMail[i])
                    filteredEmpresaProducto.add(empresaProducto[i])
                    filteredEmpresaClase.add(empresaClase[i])
                }
            }
        }
        notifyDataSetChanged()
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val empresa_id_text: TextView = itemView.findViewById(R.id.empresa_id_text)
        val empresa_nombre_text: TextView = itemView.findViewById(R.id.empresa_nombre_text)
        val empresa_url_text: TextView = itemView.findViewById(R.id.empresa_url_text)
        val empresa_telefono_text: TextView = itemView.findViewById(R.id.empresa_telefono_text)
        val mainLayout: LinearLayout = itemView.findViewById(R.id.mainLayout)
    }
}