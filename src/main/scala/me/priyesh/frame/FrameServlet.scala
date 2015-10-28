package me.priyesh.frame

import org.scalatra.RequestEntityTooLarge
import org.scalatra.servlet.{FileUploadSupport, MultipartConfig, SizeConstraintExceededException}

class FrameServlet extends FrameStack with FileUploadSupport {

  configureMultipartHandling(MultipartConfig(maxFileSize = Some(3 * 1024 * 1024)))

  private val UploadedFileKey: String = 0xf00d toString

  get("/") {
    <html>
      <body>
        <form method="post" action="/upload" enctype="multipart/form-data">
          <input type="file" name={UploadedFileKey}/>
          <input type="submit"/>
        </form>
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