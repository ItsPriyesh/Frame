package me.priyesh.frame

import java.io.{File, InputStream}

import com.sksamuel.scrimage.Image

import scala.util.{Failure, Success, Try}

object ImageProcessor {

  private lazy val frameImage = Image.fromStream(getClass.getResourceAsStream("/assets/time_steel_black_front.png"))

  private val ValidDimensions = Set(
    (144, 168), (180, 180)
  )

  def imageFrom(stream: InputStream): Option[Image] = Try(Image.fromStream(stream)) toOption

  def hasValidDimensions(image: Image): Boolean = ValidDimensions contains image.dimensions

  def overlay(image: Image): File = image.underlay(frameImage).output(new File("./output.png"))

}
