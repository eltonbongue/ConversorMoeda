package com.example.conversordemoeda

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.conversordemoeda.api.Endpoint
import com.example.conversordemoeda.util.NetworkUtils
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Response


class MainActivity : AppCompatActivity() {

    private  lateinit var spFrom: Spinner
    private lateinit var spTo: Spinner
    private lateinit var btConvert: Button
    private lateinit var tvResult: TextView
    private lateinit var valueFrom: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        spFrom = findViewById(R.id.spFrom)
        spTo = findViewById(R.id.spTo)
        btConvert = findViewById(R.id.btConvert)
        tvResult = findViewById(R.id.tvResult)
        valueFrom = findViewById(R.id.valueFrom)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        getConversoes()
        btConvert.setOnClickListener { converteMoney() }
    }

    fun converteMoney() {
        val valor = valueFrom.text.toString().toDoubleOrNull()
        if (valor == null) {
            tvResult.text = getString(R.string.invalid_value)
            return
        }

        val retrofitClient = NetworkUtils.getRetrofitInstance("https://cdn.jsdelivr.net/")
        val endpoint = retrofitClient.create(Endpoint::class.java)

        endpoint.getTaxaCambio(
            spFrom.selectedItem.toString(),
            spTo.selectedItem.toString()
        ).enqueue(object : retrofit2.Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful && response.body() != null) {
                    val moedaDestino = spTo.selectedItem.toString().lowercase()
                    val taxa = response.body()?.get(moedaDestino)?.asDouble

                    if (taxa != null) {
                        val resultado = valor * taxa
                        tvResult.text = getString(R.string.result_text,resultado)
                    } else {
                        tvResult.text = getString(R.string.rate_not_found)
                    }
                } else {
                    tvResult.text = getString(R.string.api_error)
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                tvResult.text = getString(R.string.request_failed)
                Log.e("API_ERROR", "Erro de requisição", t)
            }
        })
    }



    fun getConversoes(){

        val retrofitCliente = NetworkUtils.getRetrofitInstance("https://cdn.jsdelivr.net/")
        val endpoint = retrofitCliente.create(Endpoint::class.java)

        endpoint.getConversoes().enqueue(object : retrofit2.Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject?>, response: Response<JsonObject?>) {

                var data = mutableListOf<String>()

                response.body()?.keySet()?.iterator()?.forEach {
                    data.add(it)
                }
                val posBrl = data.indexOf("brl")
                val posUsd = data.indexOf("usd")

                val adapter = ArrayAdapter(baseContext, android.R.layout.simple_spinner_dropdown_item,data)
                spFrom.adapter = adapter
                spTo.adapter = adapter


                spFrom.setSelection(posBrl)
                spTo.setSelection(posUsd)

            }

            override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
            println("está mal")
            }



        })
    }
}