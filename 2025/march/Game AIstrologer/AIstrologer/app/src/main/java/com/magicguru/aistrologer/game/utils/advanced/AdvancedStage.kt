package com.magicguru.aistrologer.game.utils.advanced

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.Disposable
import com.badlogic.gdx.utils.viewport.Viewport

open class AdvancedStage(viewport: Viewport) : Stage(viewport) {

    fun addActors(vararg actors: Actor) {
        actors.forEach { addActor(it) }
    }

    fun addActors(actors: List<Actor>) {
        actors.forEach { addActor(it) }
    }

    fun addAndFillActor(actor: Actor) {
        addActor(actor)
        actor.setSize(width, height)
    }

    fun addAndFillActors(actors: List<Actor>) {
        actors.forEach { addActor(it.also { a -> a.setSize(width, height) }) }
    }

    fun addAndFillActors(vararg actors: Actor) {
        actors.forEach { addActor(it.also { a -> a.setSize(width, height) }) }
    }

    fun render() {
        viewport.apply()
        act()
        draw()
    }

    override fun dispose() {
        actors.onEach { actor -> if (actor is Disposable) actor.dispose() }
        super.dispose()
    }

    fun hideKeyboard() {
        Gdx.input.setOnscreenKeyboardVisible(false) // Закриваємо клавіатуру
        unfocusAll() // Знімаємо фокус з усіх акторів

        // Перевіряємо та очищаємо фокус для текстових полів
        keyboardFocus = null
        scrollFocus = null
    }

}