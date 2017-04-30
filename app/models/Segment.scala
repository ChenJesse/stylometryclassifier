package models

import breeze.linalg.DenseVector

/**
  * Created by jessechen on 4/29/17.
  */
class Segment(tokens: IndexedSeq[IndexedSeq[String]]) extends Vectorizable {
  val dimension = 10000

  def vectorize(): DenseVector[Double] = {
    val vectorArray = Array.fill[Double](dimension)(0)
//    tokens().foreach { token =>
//      val index = Math.abs(token.hashCode()) % dimension
//      vectorArray(index) = 1.0
//    }
    DenseVector[Double](vectorArray)
  }
}
