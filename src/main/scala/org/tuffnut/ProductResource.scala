package org.tuffnut

import javax.ws.rs.{QueryParam, GET, Path, Produces}
import javax.ws.rs.core.MediaType
import scala.concurrent.Await
import scala.concurrent.duration.DurationDouble
import java.io.{FileOutputStream, File}

@Produces(Array(MediaType.APPLICATION_JSON))
@Path("/products/")
class ProductResource extends Fetcher with Parser{
  @GET
  def search(@QueryParam("q") q:String) = {
    val inputFile = new File("/tmp/tuffnut/" + q + ".html")
    val html = inputFile.exists() match {
      case true =>
        val source = scala.io.Source.fromFile(inputFile,  "iso-8859-1")
       source.getLines().mkString
      case false =>
        val url = "http://www.mysmartprice.com/books/msp_book_single.php?q=" + q
        val h = Await.result(fetch(url), 60 seconds)
        saveFile(inputFile,h)
        h
    }
    parse(html)
  }


  private def saveFile(inputFile: File, html: String) = {
    val folder = inputFile.getParentFile
    if (!folder.exists && !folder.mkdirs) {
      throw new Exception()
    }
    new FileOutputStream(inputFile).write(html.map(_.toByte).toArray)
  }
}
