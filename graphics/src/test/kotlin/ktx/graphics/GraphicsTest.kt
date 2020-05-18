package ktx.graphics

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.backends.lwjgl.LwjglNativesLoader
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.glutils.FrameBuffer
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.badlogic.gdx.math.Matrix4
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.spy
import com.nhaarman.mockitokotlin2.verify
import java.io.File
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Test

/**
 * Tests general utilities related to LibGDX graphics API.
 */
class GraphicsTest {
  @Test
  fun `should begin and end Batch`() {
    val batch = mock<Batch>()

    batch.use {
      verify(batch).begin()
      assertSame(batch, it)
      verify(batch, never()).end()
    }
    verify(batch).end()
    verify(batch, never()).projectionMatrix = any()
  }

  @Test
  fun `should set projection matrix`() {
    val batch = mock<Batch>()
    val matrix = Matrix4((0..15).map { it.toFloat() }.toFloatArray())

    batch.use(matrix) {
      verify(batch).projectionMatrix = matrix
      verify(batch).begin()
      assertSame(batch, it)
      verify(batch, never()).end()
    }
    verify(batch).end()
  }

  @Test
  fun `should use Batch exactly once`() {
    val batch = mock<Batch>()
    val variable: Int

    batch.use {
      variable = 42
    }

    assertEquals(42, variable)
  }

  @Test
  fun `should set projection matrix if a camera is passed`() {
    val batch = mock<Batch>()
    val camera = OrthographicCamera()

    batch.use(camera) {
      verify(batch).projectionMatrix = camera.combined
      verify(batch).begin()
      assertSame(batch, it)
      verify(batch, never()).end()
    }
    verify(batch).end()
  }

  @Test
  fun `should use Batch with camera exactly once`() {
    val batch = mock<Batch>()
    val variable: Int

    batch.use(OrthographicCamera()) {
      variable = 42
    }

    assertEquals(42, variable)
  }

  @Test
  fun `should begin with provided projection matrix`() {
    val batch = mock<Batch>()
    val matrix = Matrix4(FloatArray(16) { it.toFloat() })

    batch.begin(projectionMatrix = matrix)

    verify(batch).projectionMatrix = matrix
    verify(batch).begin()
  }

  @Test
  fun `should use Batch with projection matrix exactly once`() {
    val batch = mock<Batch>()
    val variable: Int

    batch.use(Matrix4()) {
      variable = 42
    }

    assertEquals(42, variable)
  }

  @Test
  fun `should begin with provided camera combined matrix`() {
    val batch = mock<Batch>()
    val camera = OrthographicCamera()

    batch.begin(camera = camera)

    verify(batch).projectionMatrix = camera.combined
    verify(batch).begin()
  }

  @Test
  fun `should begin and end ShaderProgram`() {
    val shaderProgram = mock<ShaderProgram>()

    shaderProgram.use {
      verify(shaderProgram).begin()
      assertSame(shaderProgram, it)
      verify(shaderProgram, never()).end()
    }
    verify(shaderProgram).end()
  }

  @Test
  fun `should use ShaderProgram exactly once`() {
    val shaderProgram = mock<ShaderProgram>()
    val variable: Int

    shaderProgram.use {
      variable = 42
    }

    assertEquals(42, variable)
  }

  @Test
  fun `should begin and end FrameBuffer`() {
    val frameBuffer = mock<FrameBuffer>()

    frameBuffer.use {
      verify(frameBuffer).begin()
      assertSame(frameBuffer, it)
      verify(frameBuffer, never()).end()
    }
    verify(frameBuffer).end()
  }

  @Test
  fun `should use FrameBuffer exactly once`() {
    val frameBuffer = mock<FrameBuffer>()
    val variable: Int

    frameBuffer.use {
      variable = 42
    }

    assertEquals(42, variable)
  }

  @Test
  fun `should take screenshot`() {
    LwjglNativesLoader.load()
    Gdx.gl = mock()
    Gdx.graphics = mock {
      on { backBufferHeight } doReturn 4
      on { backBufferWidth } doReturn 4
    }
    val fileHandle = spy(FileHandle(File.createTempFile("screenshot", ".png")))

    takeScreenshot(fileHandle)

    verify(fileHandle).write(false)
  }
}
