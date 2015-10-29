package me.priyesh.frame

import org.scalatra.RequestEntityTooLarge
import org.scalatra.servlet.{FileUploadSupport, MultipartConfig, SizeConstraintExceededException}

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
      </head>
      <body>

        <form action="upload" class="dropzone vertical-center dropzone-background" method="post" enctype="multipart/form-data">
        </form>

        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
        <script src="js/bootstrap.min.js"></script>
        <script src="js/dropzone.js"></script>
      </body>
    </html>
  }

  post("/upload") {
    val fileStream = fileParams(UploadedFileKey).getInputStream
    if (ImageProcessor.isValidImage(fileStream)) {
      ImageProcessor.overlay(fileStream)
    } else {

    }
  }

  error {
    case e: SizeConstraintExceededException => RequestEntityTooLarge("too much!")
  }
}