package com.magicguru.aistrologer.util.utilChatGPT

data class ChatRequest(
    val model: String = "gpt-4o-mini",
    val store: Boolean = true,
    val messages: List<Message>
)

data class Message(
    val role: String, // "system", "user", або "assistant"
    val content: String
)

data class ChatResponse(
    val choices: List<Choice>
)

data class Choice(
    val message: Message
)