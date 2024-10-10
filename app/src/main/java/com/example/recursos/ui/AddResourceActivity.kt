package com.example.recursos.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.recursos.R
import com.example.recursos.model.Recurso
import com.example.recursos.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddResourceActivity : AppCompatActivity() {

    private lateinit var titleEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var linkEditText: EditText
    private lateinit var imageEditText: EditText
    private lateinit var addButton: Button
    private var resourceId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_resource)

        titleEditText = findViewById(R.id.titleEditText)
        descriptionEditText = findViewById(R.id.descriptionEditText)
        linkEditText = findViewById(R.id.linkEditText)
        imageEditText = findViewById(R.id.imageEditText)
        addButton = findViewById(R.id.addButton)

        // Recibir el ID del recurso si se está editando
        resourceId = intent.getStringExtra("RESOURCE_ID")
        resourceId?.let { loadResource(it) }

        addButton.setOnClickListener {
            if (resourceId != null) {
                editResource(resourceId!!)
            } else {
                addResource()
            }
        }
    }

    private fun loadResource(id: String) {
        RetrofitClient.api.getRecursoById(id).enqueue(object : Callback<Recurso> {
            override fun onResponse(call: Call<Recurso>, response: Response<Recurso>) {
                if (response.isSuccessful) {
                    val recurso = response.body()
                    recurso?.let {
                        titleEditText.setText(it.titulo)
                        descriptionEditText.setText(it.descripcion)
                        linkEditText.setText(it.enlace)
                        imageEditText.setText(it.imagen)
                    }
                }
            }

            override fun onFailure(call: Call<Recurso>, t: Throwable) {
                Toast.makeText(this@AddResourceActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun addResource() {
        val recurso = Recurso(
            id = "", // Dejar vacío para que la API genere un ID
            titulo = titleEditText.text.toString(),
            descripcion = descriptionEditText.text.toString(),
            enlace = linkEditText.text.toString(),
            imagen = imageEditText.text.toString()
        )

        RetrofitClient.api.createRecurso(recurso).enqueue(object : Callback<Recurso> {
            override fun onResponse(call: Call<Recurso>, response: Response<Recurso>) {
                if (response.isSuccessful) {
                    setResult(Activity.RESULT_OK)
                    finish()
                } else {
                    Toast.makeText(this@AddResourceActivity, "Error al agregar recurso", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Recurso>, t: Throwable) {
                Toast.makeText(this@AddResourceActivity, "Error de conexión: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun editResource(id: String) {
        val recurso = Recurso(
            id = id,
            titulo = titleEditText.text.toString(),
            descripcion = descriptionEditText.text.toString(),
            enlace = linkEditText.text.toString(),
            imagen = imageEditText.text.toString()
        )

        RetrofitClient.api.updateRecurso(id, recurso).enqueue(object : Callback<Recurso> {
            override fun onResponse(call: Call<Recurso>, response: Response<Recurso>) {
                if (response.isSuccessful) {
                    setResult(Activity.RESULT_OK)
                    finish()
                } else {
                    Toast.makeText(this@AddResourceActivity, "Error al editar recurso", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Recurso>, t: Throwable) {
                Toast.makeText(this@AddResourceActivity, "Error de conexión: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
