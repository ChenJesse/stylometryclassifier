package models

import breeze.linalg.{*, DenseMatrix, DenseVector}

/**
  * Created by jessechen on 4/17/17.
  */
abstract class LinearClassifier extends Classifier {
  var w: DenseVector[Double]
  var b: Double

  /**
    *
    * @param xTe Set of vectors to be classified, nxd
    * @return Labels corresponding to xTe
    */
  def classify(xTe: DenseMatrix[Double]): DenseVector[Int] =
    new DenseVector((xTe(*, ::) dot w).toArray.map(x => sign(x + b)))

  /**
    *
    * @param xTe Test set, nxd
    * @param yTe Labels corresponding to training set 1xn
    * @return The Zero-One loss given the labels
    */
  def test(xTe: DenseMatrix[Double], yTe: DenseVector[Int]): Double = {
    classify(xTe: DenseMatrix[Double]).toArray.zip(yTe.toArray)
      .count(x => x._1 != x._2) / yTe.length.toDouble
  }

  def sign(y: Double): Int = if (y >= 0) { 1 } else { -1 }
}
