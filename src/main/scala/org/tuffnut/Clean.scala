package org.tuffnut

object Clean {
  def title(value: String) = value.replaceAll("\\s+", " ")

  def priceText(value: String): String = {
    var priceString = ""
    var numberFound = false
    var decimalBefore = false
    for (i <- 0.to(value.length() - 1)) {
      val c = value.charAt(i)
      if (numberFound && Character.isSpaceChar(c)) return priceString
      if (Character.isDigit(c)) {
        priceString += c
        numberFound = true
        decimalBefore = false
      } else if (decimalBefore) {
        priceString = priceString.replaceAll("0.", "")
        decimalBefore = false
      }

      if ('.' == c && numberFound) {
        priceString += c
      } else if ('.' == c) {
        priceString += "0" + c
        decimalBefore = true
      }
    }
    if (!numberFound) "" else priceString
  }
}

