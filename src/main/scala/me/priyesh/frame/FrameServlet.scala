package me.priyesh.frame

import org.scalatra.RequestEntityTooLarge
import org.scalatra.servlet.{FileItem, FileUploadSupport, MultipartConfig, SizeConstraintExceededException}

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
        <form id="screenshotDropzone" action="/upload" class="dropzone vertical-center" method="post" enctype="multipart/form-data" />

        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
        <script src="js/dropzone.js"></script>
        <script>
          Dropzone.options.screenshotDropzone = {{
            paramName: {UploadedFileKey},
            init: function() {{
              this.on('success', function(file, response) {{
                console.log(response)
                if (response.startsWith('Error')) {{
                  console.log('error')
                  alert(response.split('::')[1]);
                }} else {{
                  console.log('worked');
                  document.body.innerHTML += '<a href="' + response + '" download="test_priyesh">Download</a>'
                }}
              }});
            }}
          }}
        </script>
      </body>
    </html>
  }

  post("/upload") { parseUpload(fileParams.get(UploadedFileKey)) }

  private def parseUpload(itemMaybe: Option[FileItem]): String = {
    import ImageProcessor._
    itemMaybe match {
      case None => "Error::No file uploaded"
      case Some(item) =>
        val image = imageFrom(item.getInputStream)
        image.fold("Error::Unable to parse image")(image => {
          if (hasValidDimensions(image)) "output/" + frame(image, session.getId).getName
          else "Error::Invalid image dimensions"
        })
    }
  }


  error {
    case e: SizeConstraintExceededException => RequestEntityTooLarge("too much!")
  }
}