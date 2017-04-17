package models

import breeze.linalg.{*, Axis, DenseMatrix, DenseVector, sum}
import breeze.numerics.log

/**
  * Created by jessechen on 3/28/17.
  */
class NBClassifier(dimension: Int) {

  var w: DenseVector[Double] = DenseVector.ones[Double](dimension)
  var b: Double = 0.0

  /**
    *
    * @param yTr List of labels for xTr
    * @return the probability that a sample in the training set is positive or negative,
    *         independent of its features (with +1 smoothing)
    */
  private def naiveBayesPY(yTr: DenseVector[Int]): (Double, Double) = {
    val yTrArray = yTr.toArray :+ Array(1, -1)
    val length = yTrArray.length
    val posCount = yTrArray.count(x => x == 1)
    val posRatio = posCount.toDouble / length.toDouble

    (posRatio, 1.0 - posRatio)
  }

  private def naiveBayesPXY(xTr: DenseMatrix[Double], yTr: DenseVector[Int]): (DenseVector[Double], DenseVector[Double]) = {
    val dim = xTr.cols
    val xTrSmooth = DenseMatrix.vertcat(xTr, DenseMatrix.ones[Double](2, dim))
    val yTrSmooth = DenseVector.vertcat(yTr, DenseVector(1, -1))

    val yTrSmoothPos: DenseVector[Double] = yTrSmooth.map(x => if (x == -1) 0 else x.toDouble)
    val yTrSmoothNeg: DenseVector[Double] = yTrSmooth.map(x => if (x == 1) 0 else x.toDouble)

    val posYTotal: Double = yTrSmoothPos dot sum(xTrSmooth, Axis._1)
    val negYTotal: Double = -1 * (yTrSmoothNeg dot sum(xTrSmooth, Axis._1))

    val posProb: DenseVector[Double] = (xTrSmooth(::, *) dot yTrSmoothPos).t / posYTotal
    val negProb: DenseVector[Double] = (xTrSmooth(::, *) dot yTrSmoothNeg).t / (-1 * negYTotal)

    (posProb, negProb)
  }

  private def naiveBayesCL(xTr: DenseMatrix[Double], yTr: DenseVector[Int]): (DenseVector[Double], Double) = {
    val (posThetas, negThetas) = naiveBayesPXY(xTr, yTr)
    val w: DenseVector[Double] = log(posThetas) - log(negThetas)

    val (posPi, negPi) = naiveBayesPY(yTr)
    val b = log(posPi) - log(negPi)

    return (w, b)
  }

  /**
    *
    * @param xTr Training set, nxd (n is number of data points, d is dimension)
    * @param yTr Labels corresponding to training set, 1xn
    */
  def train(xTr: DenseMatrix[Double], yTr: DenseVector[Int]): Unit = {
    val (trainedW, trainedB) = naiveBayesCL(xTr, yTr)
    w = trainedW
    b = trainedB
  }

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
