package com.example.client_server

import com.google.gson.annotations.SerializedName

data class Message(
    @SerializedName("message")
    val message: String
)