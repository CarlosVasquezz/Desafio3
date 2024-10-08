package com.example.recursos.network

import com.example.recursos.model.Recurso
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @GET("recursos")
    fun getRecursos(): Call<List<Recurso>>

    @GET("recursos/{id}")
    fun getRecursoById(@Path("id") id: String): Call<Recurso>

    @POST("recursos")
    fun createRecurso(@Body recurso: Recurso): Call<Recurso>

    @PUT("recursos/{id}")
    fun updateRecurso(@Path("id") id: String, @Body recurso: Recurso): Call<Recurso>

    @DELETE("recursos/{id}")
    fun deleteRecurso(@Path("id") id: String): Call<Void>
}

