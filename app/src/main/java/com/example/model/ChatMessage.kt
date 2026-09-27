package com.example.model

import java.util.UUID

data class ChatMessage(
    val id: String = UUID.randomUUID().toString(),
    val text: String,
    val isUser: Boolean,
    val timestamp: String = "",
    val isStreaming: Boolean = false,
    val isError: Boolean = false,
    val suggestedFollowUps: List<String> = emptyList(),
    val languageTip: String? = null,
    val idiomSpotlight: String? = null
)
