package com.wenubey.ktorandroidchat.ui.chat

import com.wenubey.ktorandroidchat.domain.model.Message

data class ChatState(
    val messages: List<Message> = emptyList(),
    val isLoading: Boolean = false,
)
