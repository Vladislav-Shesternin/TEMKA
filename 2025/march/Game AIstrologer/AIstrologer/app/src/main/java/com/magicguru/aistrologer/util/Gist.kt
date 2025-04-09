package com.magicguru.aistrologer.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit
import kotlin.collections.toString
import kotlin.io.readBytes

object Gist {

    private const val URL_STRING = "https://gist.githubusercontent.com/ValenDula/0abfc0e73bcdcbf4b4a732a1f3020a69/raw/com.magicguru.aistrologer"

    suspend fun getDataJson(): DataJSON? = withContext(Dispatchers.IO) {
        try {
            val url        = URL(URL_STRING)
            val connection = url.openConnection() as HttpURLConnection

            connection.requestMethod  = "GET"
            connection.connectTimeout = 5000  // 5 секунд таймаут на з'єднання
            connection.readTimeout    = 5000  // 5 секунд таймаут на читання
            connection.doInput        = true

            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                val inputStream: InputStream = connection.inputStream
                val response = inputStream.readBytes().toString(Charsets.UTF_8) // Читаємо відповідь у String
                inputStream.close()

                val json = JSONObject(response)
                log("GIST GET = $json")

                DataJSON(
                    link    = json.optString("link", ""),
                    key     = json.optString("key" , ""),
                    broken  = json.optString("broken" , ""),
                )
            } else {
                log("Gist = HTTP Error: ${connection.responseCode}")
                null
            }
        } catch (e: Exception) {
            log("Gist = Exception: ${e.message}")
            null
        }
    }

    data class DataJSON(
        val link: String,
        val key: String,
        val broken: String,
    )

}