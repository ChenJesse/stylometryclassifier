package classifiers

import breeze.linalg.DenseVector

/**
  * Created by jessechen on 4/1/17.
  */
trait RegType
case object L1Reg extends RegType
case object L2Reg extends RegType

case class Regularization(lambda: Double, reg: RegType) {
  def regGradient(w: DenseVector[Double]): DenseVector[Double] = reg match {
    case L1Reg => DenseVector(w.toArray.map(x => sign(x).toDouble)) * lambda
    case L2Reg => w * (2 * lambda)
  }

  def sign(y: Double): Int = if (y >= 0) { 1 } else { -1 }
}
