package me.priyesh.frame

import java.io.{File, InputStream}

import com.sksamuel.scrimage.Image

import scala.util.{Failure, Success, Try}

object ImageProcessor {

  private val ValidDimensions = Set(
    (144, 168), (180, 180)
  )

  def isValidImage(imageStream: InputStream): Boolean = {
    tryParseImage(imageStream) match {
      case Success(image) => ValidDimensions contains image.dimensions
      case Failure(_) => false
    }
  }
  
  def tryParseImage(imageStream: InputStream): Try[Image] = Try(Image.fromStream(imageStream))

  def overlay(imageStream: InputStream): Unit = {
    val frameStream = getClass.getResourceAsStream("/assets/time_steel_black_front.png")
    val output = new File("./output.png")
    Image
      .fromStream(imageStream)
      .underlay(Image.fromStream(frameStream))
      .output(output)
  }
}
