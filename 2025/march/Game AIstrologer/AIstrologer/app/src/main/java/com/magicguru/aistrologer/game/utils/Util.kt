package com.magicguru.aistrologer.game.utils

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.utils.Disposable
import com.badlogic.gdx.utils.viewport.Viewport
import com.magicguru.aistrologer.game.GDXGame
import kotlinx.coroutines.Runnable
import kotlin.random.Random

typealias Acts = Actions

val gdxGame: GDXGame get() = Gdx.app.applicationListener as GDXGame

val Texture.region: TextureRegion get() = TextureRegion(this)
val Float.toMS: Long get() = (this * 1000).toLong()
val Viewport.zeroScreenVector: Vector2 get() = project(Vector2(0f, 0f))
val TextureEmpty get() = Texture(1, 1, Pixmap.Format.Alpha)

fun disposeAll(vararg disposable: Disposable?) {
    disposable.forEach { it?.dispose() }
}

fun currentTimeMinus(time: Long) = System.currentTimeMillis().minus(time)

fun Collection<Disposable>.disposeAll() {
    onEach { it.dispose() }
}

fun InputMultiplexer.addProcessors(vararg processor: InputProcessor) {
    processor.onEach { addProcessor(it) }
}

fun runGDX(block: Runnable) {
    Gdx.app.postRunnable { block.run() }
}

fun TextureRegion.toTexture(): Texture {
    val pixmap = Pixmap(regionWidth, regionHeight, Pixmap.Format.RGBA8888)

    if (texture.textureData.isPrepared.not()) texture.textureData.prepare()
    val texturePixmap = texture.textureData.consumePixmap()

    pixmap.drawPixmap(texturePixmap, 0, 0, regionX, regionY, regionWidth, regionHeight)

    val newTexture = Texture(pixmap)

    texturePixmap.dispose()
    pixmap.dispose()

    return newTexture
}

fun Float.divOr0(num: Float): Float = if (num != 0f) this / num else 0f

fun Vector2.divOr0(scalar: Float): Vector2 {
    x = x.divOr0(scalar)
    y = y.divOr0(scalar)
    return this
}

fun Vector2.divOr0(scalar: Vector2): Vector2 {
    x = x.divOr0(scalar.x)
    y = y.divOr0(scalar.y)
    return this
}

private val stringBuilder = StringBuilder()

fun Number.toSeparateWithSymbol(symbol: Char): String {
    stringBuilder.clear()
    stringBuilder.append(this.toString())

    when(stringBuilder.length) {
        4 -> stringBuilder.insert(1, symbol)
        5 -> stringBuilder.insert(2, symbol)
        6 -> stringBuilder.insert(3, symbol)
        7 -> stringBuilder.insert(1, symbol).insert(5, symbol)
    }

    return stringBuilder.toString()
}

fun getZodiacIndex(date: String): Int {
    val zodiacSigns = listOf(
        Pair(20, 3),  // Aries (21.03 – 19.04)
        Pair(20, 4),  // Taurus (20.04 – 20.05)
        Pair(21, 5),  // Gemini (21.05 – 20.06)
        Pair(21, 6),  // Cancer (21.06 – 22.07)
        Pair(23, 7),  // Leo (23.07 – 22.08)
        Pair(23, 8),  // Virgo (23.08 – 22.09)
        Pair(23, 9),  // Libra (23.09 – 22.10)
        Pair(23, 10), // Scorpio (23.10 – 21.11)
        Pair(22, 11), // Sagittarius (22.11 – 21.12)
        Pair(22, 12), // Capricorn (22.12 – 19.01)
        Pair(20, 1),  // Aquarius (20.01 – 18.02)
        Pair(19, 2)   // Pisces (19.02 – 20.03)
    )

    val parts = date.split(".")
    if (parts.size != 3) return 0 // Некоректний формат дати

    val day = parts[0].toInt()
    val month = parts[1].toInt()

    for (i in zodiacSigns.indices) {
        val (startDay, startMonth) = zodiacSigns[i]
        if (month == startMonth && day >= startDay || month == startMonth + 1 && day < zodiacSigns[(i + 1) % 12].first) {
            return i
        }
    }

    return 0 // На випадок помилки
}