package me.priyesh.frame

import java.io.{File, InputStream}

import com.sksamuel.scrimage.Image

import scala.util.Try

object ImageProcessor {

  lazy val frameImage = Image.fromStream(getClass.getResourceAsStream("/assets/time_steel_black_front.png"))

  private val ValidDimensions = Set(
    (144, 168), (180, 180)
  )

  def validateImage(imageStream: InputStream): Try[Image] = {
    tryParseImage(imageStream).filter {
      image => ValidDimensions contains image.dimensions
    }
  }
  
  def tryParseImage(imageStream: InputStream): Try[Image] = Try(Image.fromStream(imageStream))

  def overlay(sessionId: String, image: Image): File = {
    def nextOutputFile(index: Int = 0): File = {
      val candidate = new File(s"./output/${sessionId}_$index.png")
      if (candidate.exists()) {
        nextOutputFile(index + 1)
      } else {
        candidate
      }
    }
    val outputFile = nextOutputFile()
    image.underlay(frameImage).output(outputFile)
  }
}
