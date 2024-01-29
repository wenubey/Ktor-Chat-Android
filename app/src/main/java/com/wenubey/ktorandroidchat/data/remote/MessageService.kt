package com.wenubey.ktorandroidchat.data.remote

import com.wenubey.ktorandroidchat.domain.model.Message

interface MessageService {

    suspend fun getAllMessages(): List<Message>

    companion object {
        const val BASE_URL = "http://192.168.0.20:8080"
    }

    sealed class Endpoints(val url: String) {
        data object GetAllMessages: Endpoints("$BASE_URL/messages")
    }
}