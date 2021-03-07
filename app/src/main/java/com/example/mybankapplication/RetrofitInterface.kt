package com.example.mybankapplication

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET


interface RetrofitInterface {

    @GET(value = "accounts")
    fun getposts() : Call<List<PostModel?>?>?


}










