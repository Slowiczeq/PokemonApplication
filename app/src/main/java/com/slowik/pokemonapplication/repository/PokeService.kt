package com.slowik.pokemonapplication.repository

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET


interface PokeService {
    @GET("/api/v2/pokemon")
    suspend fun getPokeResponse(): Response<PokeResponse>

    companion object{
        private const val BASE_URL="https://pokeapi.co/"
        private val logger =
            HttpLoggingInterceptor().apply {
                this.level = HttpLoggingInterceptor.Level.BODY }

        private val okHttp = OkHttpClient.Builder().apply {
            this.addInterceptor(logger) }.build()

        private val retrofit: Retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttp)
                .build()
        }
        val pokeService: PokeService by lazy{
            retrofit.create(PokeService::class.java)
        }
    }
}