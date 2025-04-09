# LibGDX
-dontwarn javax.annotation.Nullable

# Appsflyer
-keep class com.appsflyer.** { *; }
-keep class kotlin.jvm.internal.** { *; }
-keep public class com.android.installreferrer.** { *; }


#################################
# 🚀 Твій пакет: ChatRequest, Message, ChatResponse, Choice
#################################

# Зберегти всі поля та класи, щоб Gson міг серіалізувати/десеріалізувати
-keep class com.magicguru.aistrologer.util.utilChatGPT.ChatRequest { *; }
-keep class com.magicguru.aistrologer.util.utilChatGPT.Message { *; }
-keep class com.magicguru.aistrologer.util.utilChatGPT.ChatResponse { *; }
-keep class com.magicguru.aistrologer.util.utilChatGPT.Choice { *; }

# Зберегти анотації, якщо колись будеш додавати @SerializedName
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}
-keepattributes *Annotation*

#################################
# Retrofit + Gson
#################################

# Retrofit (інтерфейси + анотації)
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }

# Gson
-dontwarn com.google.gson.**
-keep class com.google.gson.** { *; }

# Якщо використовуєш GsonConverterFactory
-keep class retrofit2.converter.gson.GsonConverterFactory { *; }

#################################
# OkHttp
#################################

-dontwarn okhttp3.**
-keep class okhttp3.** { *; }

-dontwarn okio.**
-keep class okio.** { *; }

#################################
# Kotlin / Coroutines
#################################

-keepclassmembers class kotlinx.coroutines.** { *; }
-dontwarn kotlinx.coroutines.**

# Зберегти внутрішні класи та метадані
-keepattributes InnerClasses, EnclosingMethod

#################################
# Android SDK (опціонально)
#################################

-keep class android.support.** { *; }
-keep class androidx.** { *; }
-dontwarn android.support.**
-dontwarn androidx.**

#################################
# Optional: відключити логи в релізі (за бажанням)
#################################

-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
    public static *** w(...);
    public static *** e(...);
}