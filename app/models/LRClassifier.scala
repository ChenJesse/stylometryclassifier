package models

import breeze.linalg._
import breeze.numerics._

/**
  * Created by jessechen on 3/28/17.
  */
class LRClassifier(dimension: Int, alpha: Double, maxiter: Int, delta: Double) extends Classifier {
  var w: DenseVector[Double] = DenseVector.ones[Double](dimension)

  /**
    *
    * @param xTr Training set, nxd (n is number of data points, d is dimension)
    * @param yTr Labels corresponding to training set, 1xn
    */
  def train(xTr: DenseMatrix[Double], yTr: DenseVector[Int]): Unit = {
    adagrad(logistic, alpha, maxiter, delta, xTr, yTr)
  }

  /**
    *
    * @param xTe Set of vectors to be classified, nxd
    * @return Labels corresponding to xTe
    */
  def classify(xTe: DenseMatrix[Double]): DenseVector[Int] =
    new DenseVector((xTe(*, ::) dot w).toArray.map(x => sign(x)))

  /**
    *
    * @param lossFunc A loss funcion that returns the gradient at the given y
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
      val newW = w + (alphaGradient) /:/ (zEps).map(x => Math.sqrt(x))
      val diff = newW + (w *:* -1.0)
      if ((diff dot diff) < delta) return
      w = newW
    }
  }

  /**
    *
    * @param xTr Training set, nxd
    * @param yTr Labels corresponding to training set, 1xn
    * @return The gradient of the loss function at w
    */
  def logistic(xTr: DenseMatrix[Double],
               yTr: DenseVector[Int]): DenseVector[Double] = {
    val doubleYTR = new DenseVector(yTr.toArray.map(x => x.toDouble))
    val YWX = doubleYTR *:* (xTr(*, ::) dot w)
    var eToTheYWX = exp.inPlace(YWX)
    val numerator = xTr(::, *) *:* doubleYTR
    val denominator = eToTheYWX :+= 1.0
    val gradient = (sum(numerator(::, *) /:/ denominator, Axis._0) :*= -1.0).t

    return gradient
  }

  def sign(y: Double): Int = if (y >= 0) { 1 } else { -1 }
}