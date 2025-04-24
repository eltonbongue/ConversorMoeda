package com.example.conversordemoeda.api


import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface Endpoint {

    @GET("/npm/@fawazahmed0/currency-api@latest/v1/currencies.json")
    fun getConversoes(): Call<JsonObject>

    @GET("/npm/@fawazahmed0/currency-api@latest/v1/currencies/{from}/{to}.json")

    fun getTaxaCambio(
        @Path(value = "from", encoded = true) from: String,
        @Path(value = "to", encoded = true) to: String
    ): Call<JsonObject>
}
