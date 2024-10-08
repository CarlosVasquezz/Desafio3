package com.example.recursos.network

import com.example.recursos.model.Recurso
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("recursos") // Asegúrate de que este endpoint sea correcto
    fun getRecursos(): Call<List<Recurso>>
}
