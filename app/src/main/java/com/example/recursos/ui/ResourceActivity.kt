package com.example.recursos.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recursos.R
import com.example.recursos.model.Recurso
import com.example.recursos.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ResourceActivity : AppCompatActivity() {

    private lateinit var resourceAdapter: ResourceAdapter
    private lateinit var recursos: MutableList<Recurso>
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchEditText: EditText
    private lateinit var searchButton: Button
    private lateinit var resetButton: Button
    private lateinit var addButton: Button

    companion object {
        const val ADD_RESOURCE_REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resource)

        recursos = mutableListOf()
        resourceAdapter = ResourceAdapter(recursos) { resourceId ->
            editResource(resourceId)
        }

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = resourceAdapter

        searchEditText = findViewById(R.id.searchEditText)
        searchButton = findViewById(R.id.searchButton)
        resetButton = findViewById(R.id.resetButton)
        addButton = findViewById(R.id.addButton)

        searchButton.setOnClickListener {
            val id = searchEditText.text.toString()
            if (id.isNotBlank()) {
                searchResourceById(id)
            }
        }

        resetButton.setOnClickListener {
            resetFilter() // Restablecer filtro
        }

        addButton.setOnClickListener {
            val intent = Intent(this, AddResourceActivity::class.java)
            startActivityForResult(intent, ADD_RESOURCE_REQUEST_CODE)
        }

        fetchResources() // Obtener recursos al iniciar
    }

    private fun fetchResources() {
        RetrofitClient.api.getRecursos().enqueue(object : Callback<List<Recurso>> {
            override fun onResponse(call: Call<List<Recurso>>, response: Response<List<Recurso>>) {
                if (response.isSuccessful) {
                    recursos.clear()
                    response.body()?.let { recursos.addAll(it) }
                    resourceAdapter.updateRecursos(recursos)
                } else {
                    Toast.makeText(this@ResourceActivity, "Error al obtener recursos", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Recurso>>, t: Throwable) {
                Toast.makeText(this@ResourceActivity, "Error de conexión: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun editResource(resourceId: String) {
        val intent = Intent(this, AddResourceActivity::class.java)
        intent.putExtra("RESOURCE_ID", resourceId)
        startActivityForResult(intent, ADD_RESOURCE_REQUEST_CODE)
    }

    private fun searchResourceById(id: String) {
        RetrofitClient.api.getRecursoById(id).enqueue(object : Callback<Recurso> {
            override fun onResponse(call: Call<Recurso>, response: Response<Recurso>) {
                if (response.isSuccessful) {
                    val recurso = response.body()
                    recurso?.let {
                        recursos.clear()
                        recursos.add(it)
                        resourceAdapter.updateRecursos(recursos)
                    } ?: Toast.makeText(this@ResourceActivity, "Recurso no encontrado", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@ResourceActivity, "Error al buscar recurso", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Recurso>, t: Throwable) {
                Toast.makeText(this@ResourceActivity, "Error de conexión: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_RESOURCE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            fetchResources() // Llama al método para obtener todos los recursos
        }
    }

    private fun resetFilter() {
        fetchResources() // Llama a fetchResources para restablecer la lista
        searchEditText.text.clear() // Limpia el campo de búsqueda
    }
}
