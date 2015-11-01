package me.priyesh.frame

import java.io.{File, InputStream}

import com.sksamuel.scrimage.Image

import scala.util.Try

object ImageProcessor {

  private lazy val FrameImage = Image.fromStream(getClass.getResourceAsStream("/assets/time_steel_black_front.png"))

  private val ValidDimensions = Set(
    (144, 168), (180, 180)
  )

  def imageFrom(stream: InputStream): Option[Image] = Try(Image.fromStream(stream)) toOption

  def hasValidDimensions(image: Image): Boolean = ValidDimensions contains image.dimensions

  def frame(image: Image, sessionId: String): File = {
    val output = new File(s"./src/main/webapp/output/${sessionId}_framed.png")
    FrameImage.underlay(image.resizeTo(FrameImage.width, FrameImage.height)).output(output)
  }

}