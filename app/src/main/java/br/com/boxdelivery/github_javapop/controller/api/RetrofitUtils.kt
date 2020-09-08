package br.com.boxdelivery.github_javapop.controller.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitUtils {
    companion object {
        private const val GITHUB_BASE_URL = "https://api.github.com/"

        /** Return an instance of Retrofit */
        @JvmStatic
        fun getRetrofitInstance() : Retrofit = Retrofit.Builder()
            .baseUrl(GITHUB_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}