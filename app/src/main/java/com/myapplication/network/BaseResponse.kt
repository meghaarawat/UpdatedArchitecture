package com.myapplication.network

data class BaseResponse(
    val status: String,
    val message: String,
    val `data`: Any
)