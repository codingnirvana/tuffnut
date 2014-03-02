package org.tuffnut

import org.jsoup.Jsoup
import scala.collection.JavaConversions._
import dispatch._
import dispatch.Defaults._

import scala.concurrent.Await
import scala.concurrent.duration.DurationDouble

case class Results(title:String, imageUrl: String, author: String, minPrice: String, offers:List[Offer])
case class Offer(storeName: String, storeLogoUrl: String, price: String, storeUrl: String)

trait Parser {

  def document(htmlStr: String) = Jsoup.parse(htmlStr)
  implicit def strToDoc(htmlString: String) = document(htmlString)

  def parseMSP(body:String):Product = {
    val htmlDocument = document(body)
    val title =  htmlDocument.select("h2#book_title").text()
    val imgUrl =  htmlDocument.select("img#book_img").text()
    val author =  htmlDocument.select("h4#book_author").text()
    val minPrice =  htmlDocument.select("div#bestprice").text()

    val bookBox = htmlDocument.select("div#book_box div.pt_row")

    val offers = bookBox.map {
      n =>
        val td = n.select("tr td").toList
        Offer(td(0).select("img").attr("alt"),td(0).select("img").attr("src"), Clean.priceText(td(1).text()), td(4).select("a").attr("href"))
    }

    Results(title , imgUrl, author, Clean.priceText(minPrice), offers.toList)
  }

  def parseJunglee(body: String): Product = {
    val hD = document(body)
    val nextUrl = hD.select("h3.title a").attr("href")
    val htmlDocument = document(Await.result(fetchInternal(nextUrl), 60 seconds))
    val title =  htmlDocument.select("h1.productTitle").text()
    val imgUrl =  htmlDocument.select("div#mainImage img").attr("src")
    val author =  htmlDocument.select("div.productByline").text()
    val minPrice =  htmlDocument.select("div#buy-box-slot span.whole-price").text()

    val offerBox = htmlDocument.select("div.offers-domestic tbody.offer-has-map")

    val offers = offerBox.map {
      n =>
        val td = n.select("tr td").toList
        val storeName = td(0).select("img").attr("alt").split("&")(0).trim()
        val storeUrl = "http://www.junglee.com" + td(4).select("a").attr("href")
        Offer(storeName,td(0).select("img").attr("src"), Clean.priceText(td(3).text()), storeUrl)
    }

    Results(title, imgUrl, author, Clean.priceText(minPrice), offers.toList)
  }

  def parseMSPSearch(body: String): Product = {
    val hD = document(body)
    val nextUrl = hD.select("div.msplistitem:eq(0) a:eq(0)").attr("href")
    val htmlDocument = document(Await.result(fetchInternal(nextUrl), 60 seconds))
    val title =  htmlDocument.select("span[itemprop=name]").text()
    val imgUrl =  htmlDocument.select("img#mspSingleImg").attr("src")
    val author =  ""
    val minPrice =  htmlDocument.select("div.prod_price").text()

    val offerBox = htmlDocument.select("div.store_pricetable")

    val offers = offerBox.map {
      n =>
        val storeLogoUrl = n.select("div.store_img a img").attr("src")
        val storeUrl = n.select("div.store_gostore a").attr("href")
        val storeName = storeUrl.split("store=")(1).split("&")(0)
        val price = Clean.priceText(n.select("div.store_price").text())
        Offer(storeName,storeLogoUrl, price, storeUrl)
    }

    Results(title, imgUrl, author, Clean.priceText(minPrice), offers.toList)
  }


  private def fetchInternal(pageUrl: String) = {
    val svc = url(pageUrl)
    Http.configure(_ setFollowRedirects true)(svc OK as.String)
  }
}
