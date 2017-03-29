package models

import breeze.linalg._
import breeze.numerics._

/**
  * Created by jessechen on 3/28/17.
  */
class LRClassifier(dimension: Int) extends Classifier {
  var w: DenseVector[Double] = DenseVector.zeros[Double](dimension)

  def train(xTr: DenseMatrix[Double], yTr: DenseVector[Int]): Unit = {

  }

  def classify(xTe: DenseMatrix[Double]): DenseVector[Int] = {
    new DenseVector((xTe(*, ::) dot w).toArray.map{x => sign(x)})
  }

  /**
    *
    * @param lossFunc A loss funcion that returns the gradient at the given y
    * @param alpha Step size
    * @param maxiter Number of iterations before automatically breaking out of loop
    * @param delta Change in w before automatically ending loop
    * @param xTr Training set
    * @param yTr Labels corresponding to training set
    */
  def adagrad(lossFunc: ((DenseVector[Double], DenseMatrix[Double], DenseVector[Int]) => DenseVector[Double]),
              alpha: Double, maxiter: Int, delta: Double,
              xTr: DenseMatrix[Double], yTr: DenseVector[Int]): DenseVector[Double] = {
    var z = DenseVector.zeros[Double](dimension)
    for (_ <- 1 until maxiter) {
      var gradient = lossFunc(w, xTr, yTr)
      z = z + gradient.map {x => x * x}
      val newW = w + (gradient :*= alpha) /:/ (z :+= 0.0001).map {x => Math.sqrt(x)}
      val diff = newW + (w *:* -1.0)
      if ((diff dot diff) < delta) return newW
      w = newW
    }
    return w
  }

  /**
    *
    * @param w The weight vector
    * @param xTr Training set
    * @param yTr Labels corresponding to training set
    * @return The gradient of the loss function at w
    */
  def logistic(w: DenseVector[Double], xTr: DenseMatrix[Double],
               yTr: DenseVector[Int]): DenseVector[Double] = {
    val n = xTr.rows
    val d = xTr.cols

    val doubleYTR: DenseVector[Double] = new DenseVector(yTr.toArray.map(x => x.toDouble))
    val YWX: DenseVector[Double] = doubleYTR *:* (xTr(*, ::) dot w)
    var eToTheYWX: DenseVector[Double] = exp.inPlace(YWX)
    val numerator: DenseMatrix[Double] = xTr(*, ::) *:* doubleYTR
    val denominator: DenseVector[Double] = eToTheYWX :+= 1.0
    val gradient = (sum(numerator /:/ denominator.t, Axis._0) :*= -1.0).t

    return gradient
  }

  def sign(y: Double): Int = if (y >= 0) { 1 } else { -1 }
}
