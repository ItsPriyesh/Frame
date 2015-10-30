package me.priyesh.frame

import java.io.File

import org.scalatra.RequestEntityTooLarge
import org.scalatra.servlet.{FileUploadSupport, MultipartConfig, SizeConstraintExceededException}

import scala.util.Try

class FrameServlet extends FrameStack with FileUploadSupport {

  configureMultipartHandling(MultipartConfig(maxFileSize = Some(3 * 1024 * 1024)))

  private val UploadedFileKey: String = 0xf00d toString

  get("/") {
    <html lang="en">
      <head>
        <meta charset="utf-8"/>
        <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
        <meta name="viewport" content="width=device-width, initial-scale=1"/>
        <title>Frame</title>
        <link href="css/bootstrap.min.css" rel="stylesheet"/>
        <link href="css/styles.css" rel="stylesheet"/>
        <link href="css/dropzone.css" rel="stylesheet"/>
      </head>
      <body>

        <form id="screenshotDropzone" action="/upload" class="dropzone vertical-center" method="post" enctype="multipart/form-data">
        </form>

        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/require.js/2.1.20/require.js"></script>
        <script src="js/dropzone.js"></script>
        <script>
          Dropzone.options.screenshotDropzone = {{ paramName: {UploadedFileKey} }}
        </script>
      </body>
    </html>
  }

  post("/upload") {
    println(s"Uploaded something: ${fileParams.get(UploadedFileKey)}")
    class ImageNotFoundException(message: String = "") extends Exception
    val fileStream = Try(
      fileParams.get(UploadedFileKey).map(_.getInputStream).getOrElse(throw new ImageNotFoundException()))
    for {
      presentFile <- fileStream
      validImage <- ImageProcessor.validateImage(presentFile)
    } yield {
      val overlaidImageFile = ImageProcessor.overlay(request.getSession.getId, validImage)
      val overlaidImagePath = "." + File.separator +
        overlaidImageFile.getAbsolutePath.split(File.separator).takeRight(2).mkString(File.separator)
      <html lang="en">
        <head>
          <meta charset="utf-8"/>
          <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
          <meta name="viewport" content="width=device-width, initial-scale=1"/>
          <title>Upload</title>
          <link href="css/bootstrap.min.css" rel="stylesheet"/>
          <link href="css/styles.css" rel="stylesheet"/>
        </head>
        <body>
          <a href={overlaidImagePath}>CLICK</a>
        </body>
      </html>
    }
  }



  error {
    case e: SizeConstraintExceededException => RequestEntityTooLarge("too much!")
  }
}