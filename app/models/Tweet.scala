package models

import breeze.linalg.DenseVector

/**
  * Created by jessechen on 4/25/17.
  */
class Tweet(text: String, created: String) extends Text(text){
  val dimension = 10000

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

object Tweet {
  def apply(s: String, date: String) = new Tweet(s, date)
}