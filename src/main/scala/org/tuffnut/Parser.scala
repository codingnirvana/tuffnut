package org.tuffnut

import org.jsoup.Jsoup
import scala.collection.JavaConversions._

case class Results(title:String, imageUrl: String, author: String, minPrice: String, offers:List[Offer])
case class Offer(storeName: String, storeLogoUrl: String, price: String, storeUrl: String)

trait Parser {

  def document(htmlStr: String) = Jsoup.parse(htmlStr)
  implicit def strToDoc(htmlString: String) = document(htmlString)

  def parse(body:String):Product = {
    val htmlDocument = document(body)
    val title =  htmlDocument.select("h2#book_title").text()
    val imgUrl =  htmlDocument.select("img#book_img").text()
    val author =  htmlDocument.select("h4#book_author").text()
    val minPrice =  htmlDocument.select("div#bestprice").text()

    val bookBox = htmlDocument.select("div#book_box div.pt_row")

    val offers = bookBox.map {
      n =>
        val td = n.select("tr td").toList
        Offer(td(0).select("img").attr("alt"),td(0).select("img").attr("src"), td(1).text(), td(4).select("img").attr("src"))
    }

    Results(title , imgUrl, author, minPrice, offers.toList)
  }
}
