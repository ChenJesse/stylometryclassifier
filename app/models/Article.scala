package models

import breeze.linalg.DenseVector

/**
  * Created by jessechen on 3/27/17.
  */

class Article(text: String, date: String, source: Source) {
  def sanitize(): String = {
    val sanitizedText = text.trim()
      .replaceAll('\"'.toString, "")
      .replaceAll("[", "")
      .replaceAll("]", "")
      .replaceAll("(", "")
      .replaceAll(")", "")
    val stopWords = scala.io.Source.fromFile("app/resources/stopwords.txt").mkString
      .split('\n').map(word => " " + word + " ")
    stopWords.foldLeft(sanitizedText)((sanitizedText, stopWord) => sanitizedText.replaceAll(stopWord, " "))
  }

  def vectorize(dimension: Int): DenseVector[Double] = {
    val vectorArray = Array.fill[Double](dimension)(0)
    var tokens = text.trim().split(Array('.', ' ', '!', ';', ':', '?'))
    tokens = tokens.map(token => token.trim())
    tokens.foreach {token =>
      val index = Math.abs(token.hashCode()) % dimension
      vectorArray(index) = 1.0
    }
    DenseVector[Double](vectorArray)
  }
}

object Article {
  def apply(s: String, date: String, source: Source) = new Article(s, date, source)
}
