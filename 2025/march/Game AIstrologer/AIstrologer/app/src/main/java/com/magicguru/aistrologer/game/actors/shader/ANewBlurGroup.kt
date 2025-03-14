package com.magicguru.aistrologer.game.actors.shader

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.*
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.glutils.FrameBuffer
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.ScreenUtils
import com.magicguru.aistrologer.game.utils.advanced.AdvancedGroup
import com.magicguru.aistrologer.game.utils.advanced.AdvancedScreen
import com.magicguru.aistrologer.game.utils.disposeAll

class ANewBlurGroup(
    override val screen: AdvancedScreen,
): AdvancedGroup() {

    companion object {
        private var vertexShader   = Gdx.files.internal("shader/defaultVS.glsl").readString()
        private var fragmentShader = Gdx.files.internal("shader/testBackgroundFS.glsl").readString()
    }

    private var shaderProgram: ShaderProgram? = null

    private var fboSceneBack: FrameBuffer?   = null
    private var fboSceneUI  : FrameBuffer?   = null
    private var fboScene    : FrameBuffer?   = null
    private var fboBlur     : FrameBuffer?   = null

    private var textureSceneBack: TextureRegion? = null
    private var textureSceneUI  : TextureRegion? = null
    private var textureScene    : TextureRegion? = null
    private var textureBlur     : TextureRegion? = null

    private var camera = OrthographicCamera()

    private val stagePosition = Vector2()
    private val tmpVector2    = Vector2(0f, 0f)

    var radiusBlur = 0f

    override fun addActorsOnGroup() {
        createShaders()
        createFrameBuffer()
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        if (batch         == null ||
            shaderProgram == null ||
            fboScene      == null || fboBlur == null
        ) return

        batch.end()

        captureScreenBack(batch)
        captureScreenUI(batch)
        captureScreenAll(batch)
        applyBlurShader(batch)

        // 3. Малюємо розмитий фон під групою та саму групу
        batch.begin()

        batch.shader = null

        //batch.projectionMatrix = camera.combined
        //batch.draw(textureBlur, x, y, width, height)

        batch.projectionMatrix = screen.stageUI.camera.combined
        //batch.draw(textureBlur, x, y, width, height)

        batch.draw(
            textureBlur,
            x, y,
            originX, originY,
            width, height,
            scaleX, scaleY,
            rotation,
        )

        //batch.projectionMatrix = stage.camera.combined
        super.draw(batch, parentAlpha) // Звичайний контент групи поверх

        batch.end()
        batch.begin()
    }

    override fun dispose() {
        super.dispose()
        disposeAll(
            shaderProgram,
            fboScene, fboBlur,
        )
    }

    // Logic ------------------------------------------------------------------------

    private fun createShaders() {
        ShaderProgram.pedantic = true
        shaderProgram = ShaderProgram(vertexShader, fragmentShader)

        if (shaderProgram?.isCompiled == false) {
            throw IllegalStateException("shader compilation failed:\n" + shaderProgram?.log)
        }
    }

    private fun createFrameBuffer() {
        camera = OrthographicCamera(width, height)
        camera.update()

        fboSceneBack = FrameBuffer(Pixmap.Format.RGBA8888, width.toInt(), height.toInt(), false)
        fboSceneUI   = FrameBuffer(Pixmap.Format.RGBA8888, width.toInt(), height.toInt(), false)
        fboScene     = FrameBuffer(Pixmap.Format.RGBA8888, width.toInt(), height.toInt(), false)
        fboBlur      = FrameBuffer(Pixmap.Format.RGBA8888, width.toInt(), height.toInt(), false)

        textureSceneBack = TextureRegion(fboSceneBack!!.colorBufferTexture).apply { flip(false, true) }
        textureSceneUI   = TextureRegion(fboSceneUI!!.colorBufferTexture).apply { flip(false, true) }
        textureScene     = TextureRegion(fboScene!!.colorBufferTexture).apply { flip(false, true) }
        textureBlur      = TextureRegion(fboBlur!!.colorBufferTexture).apply { flip(false, true) }
    }

    private fun captureScreenBack(batch: Batch) {
        screen.viewportBack.apply()
        stagePosition.set(localToStageCoordinates(tmpVector2.set(0f, 0f)))

        // 1. Захоплюємо всю сцену до рендеру групи
        fboSceneBack!!.begin()
        ScreenUtils.clear(Color.CLEAR)

        // Отримуємо екранні координати (перетворюємо їх у пікселі)
        val bottomLeft = Vector2(stagePosition.x, stagePosition.y)
        val topRight   = Vector2(stagePosition.x + width, stagePosition.y + height)

        // Конвертуємо координати з FitViewport у ScreenViewport
        screen.viewportUI.project(bottomLeft)
        screen.viewportUI.project(topRight)

        // Отримуємо екранні значення
        val screenX      = bottomLeft.x
        val screenY      = bottomLeft.y
        val screenWidth  = topRight.x - bottomLeft.x
        val screenHeight = topRight.y - bottomLeft.y

        camera.setToOrtho(false, screenWidth, screenHeight)
        camera.position.set(screenX + screenWidth / 2f, screenY + screenHeight / 2f, 0f)
        camera.update()

        batch.projectionMatrix = camera.combined

        batch.begin()

        //isVisible = false
        screen.stageBack.root.draw(batch, 1f)
        //isVisible = true

        batch.setColor(Color.WHITE)

        batch.end()
        fboSceneBack!!.end()
        screen.stageUI.viewport.apply()
    }

    private fun captureScreenUI(batch: Batch) {
        screen.stageUI.viewport.apply()
        stagePosition.set(localToStageCoordinates(tmpVector2.set(0f, 0f)))

        // 1. Захоплюємо всю сцену до рендеру групи
        fboSceneUI!!.begin()
        ScreenUtils.clear(Color.CLEAR)

        camera.setToOrtho(false, width, height)
        camera.position.set(stagePosition.x + (width / 2f), stagePosition.y + (height / 2f), 0f)
        camera.update()
        batch.projectionMatrix = camera.combined
        //batch.setBlendFunction(GL20.GL_ONE_MINUS_DST_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)

        batch.begin()

        isVisible = false
        screen.stageUI.root.draw(batch, 1f)
        isVisible = true

        batch.setColor(Color.WHITE)

        batch.end()
        fboSceneUI!!.end()
        screen.stageUI.viewport.apply()
    }

    private fun captureScreenAll(batch: Batch) {
        screen.stageUI.viewport.apply()
        stagePosition.set(localToStageCoordinates(tmpVector2.set(0f, 0f)))

        // 1. Захоплюємо всю сцену до рендеру групи
        fboScene!!.begin()
        ScreenUtils.clear(Color.CLEAR)

        camera.setToOrtho(false, width, height)
        camera.position.set(stagePosition.x + (width / 2f), stagePosition.y + (height / 2f), 0f)
        camera.update()
        batch.projectionMatrix = camera.combined

        batch.begin()

        batch.draw(textureSceneBack, x, y, width, height)
        batch.draw(textureSceneUI, x, y, width, height)

        batch.setColor(Color.WHITE)

        batch.end()
        fboScene!!.end()

        screen.stageUI.viewport.apply()
    }

    private fun applyBlurShader(batch: Batch) {
        screen.stageUI.viewport.apply()
        stagePosition.set(localToStageCoordinates(tmpVector2.set(0f, 0f)))

        // 2. Background (Horizontal and Vertical) Blur
        fboBlur!!.begin()
        ScreenUtils.clear(Color.CLEAR, true)

        camera.setToOrtho(false, width, height)
        camera.position.set(stagePosition.x + (width / 2f), stagePosition.y + (height / 2f), 0f)
        camera.update()
        batch.projectionMatrix = camera.combined

        batch.begin()

        batch.shader = shaderProgram

        Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0)
        textureScene!!.texture.bind(0)
        shaderProgram!!.setUniformi("u_texture", 0)
        shaderProgram!!.setUniformf("u_groupSize", width, height)
        shaderProgram!!.setUniformf("u_blurAmount", radiusBlur)
        shaderProgram!!.setUniformf("u_direction", 1f, 0f)

        batch.draw(textureScene, x, y, width, height)

        shaderProgram!!.setUniformf("u_direction", 0f, 1f)

        batch.draw(textureScene, x, y, width, height)

        //batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)
        batch.setColor(Color.WHITE)

        batch.end()
        fboBlur!!.end()
        screen.stageUI.viewport.apply()
    }

}