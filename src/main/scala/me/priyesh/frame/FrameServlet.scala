package me.priyesh.frame

import org.scalatra.servlet.{MultipartConfig, FileUploadSupport}

class FrameServlet extends FrameStack with FileUploadSupport {

  configureMultipartHandling(MultipartConfig(maxFileSize = Some(3 * 1024 * 1024)))

  private val UploadedFileKey: String = 0xf00d toString

  get("/") {
    <form method="post" action="/upload" enctype="multipart/form-data">
      <input type="file" name={UploadedFileKey}/>
      <input type="submit"/>
    </form>
  }

  post("/upload") {
    print(fileParams(UploadedFileKey).getSize)
  }

}
