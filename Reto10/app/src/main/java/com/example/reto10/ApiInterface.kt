package com.example.reto10


import retrofit2.Call
import retrofit2.http.GET

interface ApiInterface {

    @GET("qijw-htwa.json")
    fun getData(): Call<List<MyDataItem>>
}