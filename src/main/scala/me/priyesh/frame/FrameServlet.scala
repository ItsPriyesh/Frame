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
        <title>Bootstrap 101 Template</title>
        <link href="css/bootstrap.min.css" rel="stylesheet"/>
      </head>
      <body>
        <h1>Hello, world!</h1>

        <form action="/file-upload"
              class="dropzone"
              id="my-awesome-dropzone"></form>

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