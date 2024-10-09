package com.example.recursos.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recursos.R
import com.example.recursos.model.Recurso
import android.content.Intent
import android.net.Uri


class ResourceAdapter(private var recursos: List<Recurso>) : RecyclerView.Adapter<ResourceAdapter.ResourceViewHolder>() {

    class ResourceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
        val imageView: ImageView = itemView.findViewById(R.id.imageView) // Asegúrate de que el ID coincida
        val linkTextView: TextView = itemView.findViewById(R.id.linkTextView) // Asegúrate de que el ID coincida
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResourceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recurso, parent, false)
        return ResourceViewHolder(view)
    }

    override fun onBindViewHolder(holder: ResourceViewHolder, position: Int) {
        val recurso = recursos[position]
        holder.titleTextView.text = recurso.titulo
        holder.descriptionTextView.text = recurso.descripcion

        // Cargar la imagen usando Glide
        Glide.with(holder.itemView.context)
            .load(recurso.imagen)
            .into(holder.imageView)

        // Mostrar "Enlace al recurso"
        holder.linkTextView.text = "Enlace al recurso"

        // Configurar el click en el link
        holder.linkTextView.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(recurso.enlace))
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount() = recursos.size

    fun updateRecursos(newRecursos: List<Recurso>) {
        recursos = newRecursos
        notifyDataSetChanged()
    }
}