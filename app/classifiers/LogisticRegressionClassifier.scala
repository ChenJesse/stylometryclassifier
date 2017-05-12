package classifiers

import breeze.linalg.{*, Axis, DenseMatrix, DenseVector, norm, sum}
import breeze.numerics.exp

/**
  * Created by jessechen on 3/28/17.
  */
class LogisticRegressionClassifier(dimension: Int, reg: Option[Regularization] = None, alpha: Double = 1,
                                   maxiter: Int = 1000, delta: Double = 0.001) extends LinearClassifier(dimension) {
  var w = DenseVector.rand[Double](dimension)
  var b = 0.0

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
    * @param xTr Training set, nxd
    * @param yTr Labels corresponding to training set, 1xn
    * @return The gradient of the loss function at w
    */
  def logistic(xTr: DenseMatrix[Double],
               yTr: DenseVector[Int]): DenseVector[Double] = {
    val doubleYTR = new DenseVector(yTr.toArray.map(_.toDouble))
    val YWX = doubleYTR *:* (xTr(*, ::) dot w)
    var eToTheYWX = exp.inPlace(YWX)
    val numerator = xTr(::, *) *:* doubleYTR
    val denominator = eToTheYWX :+= 1.0
    val gradient = (sum(numerator(::, *) /:/ denominator, Axis._0) * -1.0).t
    reg match {
      case Some(reg) => gradient + reg.regGradient(w)
      case None => gradient
    }
  }
}