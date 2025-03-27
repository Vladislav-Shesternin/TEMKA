package com.magicguru.aistrologer.game.manager.util

import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.magicguru.aistrologer.game.manager.SpriteManager

class SpriteUtil {

     class Loader {
          private fun getRegion(name: String): TextureRegion = SpriteManager.EnumAtlas.LOADER.data.atlas.findRegion(name)

          val listCircle = List(3) { getRegion("${it.inc()}") }

          val BACKGROUND_BLUR = SpriteManager.EnumTexture.LOADER_BACKGROUND_BLUR.data.texture

     }

     class All {
          private fun getRegionAll(name: String): TextureRegion = SpriteManager.EnumAtlas.ALL.data.atlas.findRegion(name)
          private fun getRegionItems(name: String): TextureRegion = SpriteManager.EnumAtlas.ITEMS.data.atlas.findRegion(name)
          private fun get9Path(name: String): NinePatch = SpriteManager.EnumAtlas.ALL.data.atlas.createPatch(name)

          // atlas All ------------------------------------------------------------------------------

          val btn_deff    = getRegionAll("btn_deff")
          val btn_dis     = getRegionAll("btn_dis")
          val btn_press   = getRegionAll("btn_press")
          val date        = getRegionAll("date")
          val select      = getRegionAll("select")
          val top         = getRegionAll("top")
          val back_press  = getRegionAll("back_press")
          val back_def    = getRegionAll("back_def")
          val blum        = getRegionAll("blum")
          val check_def   = getRegionAll("check_def")
          val check_press = getRegionAll("check_press")
          val frame_time    = getRegionAll("frame_time")
          val pitanie_press = getRegionAll("pitanie_press")
          val pitanie_def   = getRegionAll("pitanie_def")
          val x_def         = getRegionAll("x_def")
          val x_press       = getRegionAll("x_press")
          val spinning      = getRegionAll("spinning")

          // 9.path -----------------------------------------------------------------------------------

          val input_9 = get9Path("input")

          // atlas Items ------------------------------------------------------------------------------

          val listItems = List(12) { getRegionItems("${it.inc()}") }

          // textures ------------------------------------------------------------------------------

          val BACK_FOR_ITEM     = SpriteManager.EnumTexture.BACK_FOR_ITEM.data.texture
          val BACK_FOR_ROULETTE = SpriteManager.EnumTexture.BACK_FOR_ROULETTE.data.texture
          val BACKGROUND_DEFF   = SpriteManager.EnumTexture.BACKGROUND_DEFF.data.texture
          val ROULETTE          = SpriteManager.EnumTexture.ROULETTE.data.texture
          val POCHEMU           = SpriteManager.EnumTexture.POCHEMU.data.texture
     }

}