package models

import breeze.linalg.DenseVector

/**
  * Created by jessechen on 3/27/17.
  */

class Novel(text: String) extends Text(text) {
  val dimension = 10000

  def tokenize(): Array[String] = {
    val stopWords = scala.io.Source.fromFile("app/resources/stopwords.txt").mkString
      .split('\n')
    text.split(" ").map(token =>
        token.trim()
          .toLowerCase
          .replaceAll("[-~!@#$^%&*()_+={}\\\\[\\\\]|;:\\\"'`<,>.?/\\\\\\\\]", "")
      ).filter(token => !stopWords.contains(token))
  }

  def vectorize(): DenseVector[Double] = {
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