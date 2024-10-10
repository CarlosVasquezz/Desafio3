package com.example.recursos.ui

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recursos.R
import com.example.recursos.model.Recurso
import com.example.recursos.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ResourceAdapter(
    private var recursos: List<Recurso>,
    private val onEditClick: (String) -> Unit // Callback para manejar la edición
) : RecyclerView.Adapter<ResourceAdapter.ResourceViewHolder>() {

    class ResourceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val linkTextView: TextView = itemView.findViewById(R.id.linkTextView)
        val editButton: Button = itemView.findViewById(R.id.editButton) // Botón para editar
        val deleteButton: Button = itemView.findViewById(R.id.deleteButton) // Botón para eliminar
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResourceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recurso, parent, false)
        return ResourceViewHolder(view)
    }

    override fun onBindViewHolder(holder: ResourceViewHolder, position: Int) {
        val recurso = recursos[position]
        holder.titleTextView.text = recurso.titulo ?: "Sin título" // Proporcionar valor por defecto
        holder.descriptionTextView.text = recurso.descripcion ?: "Sin descripción" // Proporcionar valor por defecto

        // Cargar la imagen usando Glide
        Glide.with(holder.itemView.context)
            .load(recurso.imagen)
            .into(holder.imageView)

        // Mostrar "Enlace al recurso"
        holder.linkTextView.text = "Enlace al recurso"

        // Configurar el click en el link
        holder.linkTextView.setOnClickListener {
            recurso.enlace?.let { enlace ->
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(enlace))
                holder.itemView.context.startActivity(intent)
            } ?: Toast.makeText(holder.itemView.context, "Enlace no disponible", Toast.LENGTH_SHORT).show()
        }

        // Configurar el botón de editar
        holder.editButton.setOnClickListener {
            onEditClick(recurso.id ?: "") // Llamar al callback para editar, asegurarse que id no sea nulo
        }

        // Configurar el botón de eliminar
        holder.deleteButton.setOnClickListener {
            deleteRecurso(recurso.id ?: "", holder.itemView)
        }
    }

    override fun getItemCount() = recursos.size

    fun updateRecursos(newRecursos: List<Recurso>) {
        recursos = newRecursos
        notifyDataSetChanged()
    }

    private fun deleteRecurso(id: String, itemView: View) {
        RetrofitClient.api.deleteRecurso(id).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    // Eliminar el recurso de la lista y notificar al adaptador
                    recursos = recursos.filter { it.id != id }
                    notifyDataSetChanged()
                    Toast.makeText(itemView.context, "Recurso eliminado", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(itemView.context, "Error al eliminar el recurso", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(itemView.context, "Error de conexión: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

}
