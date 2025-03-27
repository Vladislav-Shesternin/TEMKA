package com.magicguru.aistrologer.game.manager.util

import com.badlogic.gdx.audio.Sound
import com.magicguru.aistrologer.game.manager.AudioManager
import com.magicguru.aistrologer.game.manager.SoundManager
import kotlin.random.Random

class SoundUtil {

    val click  = AdvancedSound(SoundManager.EnumSound.click.data.sound, 1f)
    val click_send  = AdvancedSound(SoundManager.EnumSound.click_send.data.sound, 0.75f)
    val result = AdvancedSound(SoundManager.EnumSound.result.data.sound, 1f)

    // 0..100
    var volumeLevel = AudioManager.volumeLevelPercent

    var isPause = (volumeLevel <= 0f)

    fun play(advancedSound: AdvancedSound) {
        if (isPause.not()) {
            advancedSound.apply {
                sound.play((volumeLevel / 100f) * coff)
            }
        }
    }

    data class AdvancedSound(val sound: Sound, val coff: Float)

}