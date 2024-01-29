package com.wenubey.ktorandroidchat.data.remote

import com.wenubey.ktorandroidchat.data.remote.dto.MessageDto
import com.wenubey.ktorandroidchat.domain.model.Message
import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.request.*

class MessageServiceImpl(
    private val client: HttpClient
): MessageService {

    override suspend fun getAllMessages(): List<Message> {
        return try {
           client.get(MessageService.Endpoints.GetAllMessages.url).body<List<MessageDto>>()
               .map { it.toMessage() }
        } catch (e: Exception) {
            emptyList()
        }
    }
}