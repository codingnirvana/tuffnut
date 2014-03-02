package org.tuffnut

import dispatch._
import dispatch.Defaults._
import java.net.URLEncoder

trait Fetcher {
  def fetchMSP(pageUrl:String) = {
    val headlessProxy = "http://api.phantomjscloud.com/single/browser/v1/a-demo-key-with-low-quota-per-ip-address/?targetUrl="
    val svc = url(headlessProxy + URLEncoder.encode(pageUrl, "UTF-8") + "&requestType=text")
    Http.configure(_ setFollowRedirects true)(svc OK as.String)
  }

  def fetch(pageUrl: String) = {
    val svc = url(pageUrl)
    Http.configure(_ setFollowRedirects true)(svc OK as.String)
  }
}

