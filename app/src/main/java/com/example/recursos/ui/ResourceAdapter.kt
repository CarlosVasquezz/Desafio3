package com.example.recursos.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.recursos.R
import com.example.recursos.model.Recurso

class ResourceAdapter(private var recursos: List<Recurso>) : RecyclerView.Adapter<ResourceAdapter.ResourceViewHolder>() {

    class ResourceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView) // Asegúrate de que el ID coincida
        val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView) // Asegúrate de que el ID coincida
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResourceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recurso, parent, false) // Asegúrate de tener este layout
        return ResourceViewHolder(view)
    }

    override fun onBindViewHolder(holder: ResourceViewHolder, position: Int) {
        val recurso = recursos[position]
        holder.titleTextView.text = recurso.titulo
        holder.descriptionTextView.text = recurso.descripcion
    }

    override fun getItemCount() = recursos.size

    fun updateRecursos(newRecursos: List<Recurso>) {
        recursos = newRecursos
        notifyDataSetChanged()
    }
}
