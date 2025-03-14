package com.magicguru.aistrologer.util.utilChatGPT

import com.magicguru.aistrologer.game.utils.GLOBAL_listZodiacName
import com.magicguru.aistrologer.game.utils.gdxGame

object ChatGPTHelper {
    private val apiService = RetrofitClient.instance

    suspend fun getAstrologyResponse(
        userName: String,
        birthPlace: String,
        birthDate: String,
        question: String,
        onResult: (String) -> Unit
    ) {
        try {
            val messages = listOf(
                Message("system", """
                    Ты — виртуальный астролог, специализирующийся на индивидуальных консультациях. 
                    Ориентируйся на **текущую дату**, которая передается пользователем.
                    Отвечай пользователю на его вопросы, учитывая его знак зодиака, дату и место рождения. 
                    Стиль ответа должен быть вдохновляющим, астрологически обоснованным и практичным. 
                    Дай полезные рекомендации и советы, соответствующие его вопросу и характеристикам знака. 
                    Используй дружелюбный и профессиональный тон. 
                    Отвечай только на русском языке.
                    **Важно**: всегда завершай ответ следующими словами (каждое предложение на новой строке):

                    С любовью к звездам,  
                    Твой Астролог.
                """.trimIndent()),

                Message("user", """
                    Имя: $userName
                    Место рождения: $birthPlace
                    Дата рождения: $birthDate
                    Знак зодиака: ${GLOBAL_listZodiacName[gdxGame.zodiacIndex]}
                    Текущая дата: ${gdxGame.currentDate}
                    Вопрос: $question
                """.trimIndent())
            )

            val request = ChatRequest(messages = messages)
            val response = apiService.getChatResponse(request)

            val reply = response.choices.firstOrNull()?.message?.content ?: "Ошибка в ответе"

            onResult(reply) // Возвращаем ответ в UI
        } catch (e: Exception) {
            onResult("Ошибка: ${e.localizedMessage}")
        }
    }
}