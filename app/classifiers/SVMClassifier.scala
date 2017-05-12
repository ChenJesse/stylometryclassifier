package classifiers

import breeze.linalg.{*, DenseMatrix, DenseVector, norm}

/**
  * Created by jessechen on 5/11/17.
  */
class SVMClassifier(dimension: Int, reg: Option[Regularization] = None, alpha: Double = 1,
                    maxiter: Int = 1000, delta: Double = 0.001) extends LinearClassifier(dimension) {
  var w = DenseVector.rand[Double](dimension)
  var b = 0.0

  /**
    *
    * @param xTr Training set, nxd (n is number of data points, d is dimension)
    * @param yTr Labels corresponding to training set, 1xn
    */
  def train(xTr: DenseMatrix[Double], yTr: DenseVector[Int]): Unit = {
    adagrad(hinge, alpha, maxiter, delta, xTr, yTr)
  }

  /**
    *
    * @param xTr Training set, nxd
    * @param yTr Labels corresponding to training set, 1xn
    * @return The gradient of the loss function at w
    */
  def hinge(xTr: DenseMatrix[Double],
               yTr: DenseVector[Int]): DenseVector[Double] = {
    val doubleYTR = new DenseVector(yTr.toArray.map(_.toDouble))
    val YWX = doubleYTR *:* (xTr(*, ::) dot w)
    val YX = xTr(::, *) *:* doubleYTR
    val delta = (DenseVector.ones[Double](YWX.length) - YWX).map(x => if (x <= 0) 0.0 else 1.0)
    val gradient = (YX(::, *) dot delta) * -1.0
    reg match {
      case Some(reg) => gradient.t + reg.regGradient(w)
      case None => gradient.t
    }
  }

}
