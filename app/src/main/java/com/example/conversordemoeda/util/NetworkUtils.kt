package com.example.conversordemoeda.util

import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Retrofit

class NetworkUtils {
    companion object {

        fun getRetrofitInstance(path: String): Retrofit {
            val baseUrl = if (path.endsWith("/")) path else "$path/"

            return Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }
}
