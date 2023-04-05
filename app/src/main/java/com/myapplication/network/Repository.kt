package com.myapplication.network

// Main Repository (Authenticated) with injected dependency
class Repository : BaseRepo() {
    val apiWithoutToken: RetrofitApi = RetrofitClient.getRegisterRetrofit().create(RetrofitApi::class.java)
    val apiWithToken: RetrofitApi = RetrofitClient.getUserDetails().create(RetrofitApi::class.java)
}