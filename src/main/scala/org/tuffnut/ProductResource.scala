package org.tuffnut

import javax.ws.rs.{QueryParam, GET, Path, Produces}
import javax.ws.rs.core.MediaType
import scala.concurrent.Await
import scala.concurrent.duration.DurationDouble
import java.io.{FileOutputStream, File}
import org.apache.commons.codec.digest.DigestUtils
import java.net.URLEncoder

@Produces(Array(MediaType.APPLICATION_JSON))
@Path("/products/")
class ProductResource extends Fetcher with Parser{
  @GET
  @Path("/barcode/")
  def search(@QueryParam("q") q:String) = {
    val inputFile = new File("/tmp/tuffnut/" + q + ".html")
    val html = inputFile.exists() match {
      case true =>
        val source = scala.io.Source.fromFile(inputFile,  "iso-8859-1")
       source.getLines().mkString
      case false =>
        val url = "http://www.mysmartprice.com/books/msp_book_single.php?q=" + q
        val h = Await.result(fetchMSP(url), 60 seconds)
        saveFile(inputFile,h)
        h
    }
    parseMSP(html)
  }

  @GET
  @Path("/upc/")
  def upc(@QueryParam("q") q: String) = {
    val inputFile = new File("/tmp/tuffnut/" + q + ".html")
    val html = inputFile.exists() match {
      case true =>
        val source = scala.io.Source.fromFile(inputFile,  "iso-8859-1")
        source.getLines().mkString
      case false =>
        val url = "http://www.junglee.com/mn/search/junglee/?url=search-alias%3Daps&field-keywords=" + q
        val h = Await.result(fetch(url), 60 seconds)
        saveFile(inputFile,h)
        h
    }
    parseJunglee(html)
  }

  @GET
  @Path("/text/")
  def text(@QueryParam("q") q: String) = {
    val inputFile = new File("/tmp/tuffnut/" + DigestUtils.md5(q) + ".html")
    val html = inputFile.exists() match {
      case true =>
        val source = scala.io.Source.fromFile(inputFile,  "iso-8859-1")
        source.getLines().mkString
      case false =>
        val url = "http://www.mysmartprice.com/msp/search/search.php?q=" + URLEncoder.encode(q, "UTF-8")
        val h = Await.result(fetch(url), 60 seconds)
        saveFile(inputFile,h)
        h
    }
    parseMSPSearch(html)
  }


  private def saveFile(inputFile: File, html: String) = {
    val folder = inputFile.getParentFile
    if (!folder.exists && !folder.mkdirs) {
      throw new Exception()
    }
    new FileOutputStream(inputFile).write(html.map(_.toByte).toArray)
  }
}
