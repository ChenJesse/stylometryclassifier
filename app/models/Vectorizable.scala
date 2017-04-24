package models

import breeze.linalg.DenseVector

/**
  * Created by jessechen on 4/23/17.
  */
trait Vectorizable {
  val dimension: Int
  def vectorize(): DenseVector[Double]
}
