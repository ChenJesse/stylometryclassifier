package models

import breeze.linalg.DenseVector

/**
  * Created by jessechen on 3/27/17.
  */

class Article(text: String, date: String, source: Source) {
  def vectorize(dimension: Int): DenseVector[Double] = {
    val vectorArray = Array.fill[Double](dimension)(0)
    val tokens = text.trim().split(Array('.', ' ', '!', ';', ':', '?'))
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
