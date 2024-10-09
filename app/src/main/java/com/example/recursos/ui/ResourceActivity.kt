package com.example.recursos.ui

import android.os.Bundle
import android.util.Log
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

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ResourceAdapter // Asegúrate de tener un adaptador para tus recursos

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resource) // Asegúrate de tener este layout

        recyclerView = findViewById(R.id.recyclerView) // Asegúrate de que el ID coincida con tu XML
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ResourceAdapter(emptyList()) // Inicializa con una lista vacía
        recyclerView.adapter = adapter

        fetchRecursos()
    }

    private fun fetchRecursos() {
        RetrofitClient.api.getRecursos().enqueue(object : Callback<List<Recurso>> {
            override fun onResponse(call: Call<List<Recurso>>, response: Response<List<Recurso>>) {
                if (response.isSuccessful) {
                    val recursos = response.body() ?: emptyList()
                    Log.d("ResourceActivity", "Recursos: $recursos")
                    adapter.updateRecursos(recursos)
                } else {
                    Log.e(
                        "ResourceActivity",
                        "Error en la respuesta: ${response.code()}, ${
                            response.errorBody()?.string()
                        }"
                    )
                }
            }

            override fun onFailure(call: Call<List<Recurso>>, t: Throwable) {
                Log.e("ResourceActivity", "Error de conexión: ${t.message}")
            }
        })
    }
}