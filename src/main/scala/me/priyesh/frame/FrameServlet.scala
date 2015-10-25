package me.priyesh.frame

import org.scalatra._
import scalate.ScalateSupport

class FrameServlet extends FrameStack {

  get("/") {
    <html>
      <body>
        <h1>Hello, world!</h1>
        Say <a href="hello-scalate">hello to Scalate</a>.
      </body>
    </html>
  }

}
