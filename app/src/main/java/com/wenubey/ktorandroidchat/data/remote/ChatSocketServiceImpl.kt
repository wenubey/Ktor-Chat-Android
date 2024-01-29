package com.wenubey.ktorandroidchat.data.remote

import android.util.Log
import com.wenubey.ktorandroidchat.data.remote.dto.MessageDto
import com.wenubey.ktorandroidchat.domain.model.Message
import com.wenubey.ktorandroidchat.util.Resource
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.url
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.isActive
import kotlinx.serialization.json.Json

class ChatSocketServiceImpl(
    private val client: HttpClient,
): ChatSocketService {

    private var socket: WebSocketSession? = null

    override suspend fun initSession(username: String): Resource<Unit> {
        return try {
            socket = client.webSocketSession {
                url("${ChatSocketService.Endpoints.ChatSocket.url}?username=$username")
            }
            if (socket?.isActive == true) {
                Resource.Success(Unit)
            } else Resource.Error(CONNECTION_ERROR)
        } catch (e: Exception) {
            Log.e(TAG, "initSession:Error", e)
            Resource.Error(e.localizedMessage ?: UNKNOWN_ERROR)
        }
    }

    override suspend fun sendMessage(message: String) {
        try {
            socket?.send(Frame.Text(message))
        } catch (e: Exception) {
            Log.e(TAG, "sendMessage:Error", e)
        }
    }

    override fun observeMessages(): Flow<Message> {
        return try {
            socket?.incoming
                ?.receiveAsFlow()
                ?.filter { it is Frame.Text }
                ?.map {
                    val json = (it as? Frame.Text)?.readText() ?: ""
                    val messageDto = Json.decodeFromString<MessageDto>(json)
                    messageDto.toMessage()
                } ?: flow {  }
        } catch (e: Exception) {
            Log.e(TAG, "observeMessages:Error", e)
            flow {}
        }
    }

    override suspend fun closeSession() {
        socket?.close()
    }

    companion object {
        const val TAG = "chatSocketServiceImpl"
        const val UNKNOWN_ERROR = "Unknown error occurred."
        const val CONNECTION_ERROR = "Couldn't establish a connection."
    }
}