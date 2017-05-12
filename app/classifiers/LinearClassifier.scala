package classifiers

import breeze.linalg.{*, DenseMatrix, DenseVector, norm}

/**
  * Created by jessechen on 4/17/17.
  */
abstract class LinearClassifier(dimension: Int) extends Classifier {
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

  def loadParams(newW: DenseVector[Double], newB: Double): Unit = {
    w = newW
    b = newB
  }

  /**
    *
    * @param lossFunc A loss function that returns the gradient at the given y
    * @param alpha Step size
    * @param maxiter Number of iterations before automatically breaking out of loop
    * @param delta Change in w before automatically ending loop
    * @param xTr Training set, nxd
    * @param yTr Labels corresponding to training set, 1xn
    */
  def adagrad(lossFunc: ((DenseMatrix[Double], DenseVector[Int]) => DenseVector[Double]),
                      alpha: Double, maxiter: Int, delta: Double,
                      xTr: DenseMatrix[Double], yTr: DenseVector[Int]): Unit = {
    var z = DenseVector.zeros[Double](dimension)
    for (_ <- 1 until maxiter) {
      val gradient = lossFunc(xTr, yTr)
      z = z + gradient.map {x => x * x}
      val zEps = z :+= 0.0001
      val alphaGradient = gradient :*= alpha
      val newW = w - alphaGradient /:/ zEps.map(x => Math.sqrt(x))
      if (norm(gradient) < delta) return
      w = newW
    }
  }
}
