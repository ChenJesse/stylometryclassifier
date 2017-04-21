package models

import breeze.linalg.{*, Axis, DenseMatrix, DenseVector, sum}
import breeze.numerics.log

/**
  * Created by jessechen on 3/28/17.
  */
class NaiveBayesClassifier(dimension: Int) extends LinearClassifier {

  var w = DenseVector.ones[Double](dimension)
  var b = 0.0

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

  /**
    * Finds the probability of P(X|Y=(-1 or 1)) with +1 smoothing
    * @param xTr Training set, nxd (n is number of data points, d is dimension)
    * @param yTr Labels corresponding to training set, 1xn
    * @return A tuple with the first element being a 1xd vector of probabilities
    *         P(X_i|Y=1), and the second being a 1xd vector of probabilities
    *         P(X_i|Y=-1)
    */
  private def naiveBayesPXY(xTr: DenseMatrix[Double], yTr: DenseVector[Int]): (DenseVector[Double], DenseVector[Double]) = {
    val dim = xTr.cols
    val xTrSmooth = DenseMatrix.vertcat(xTr, DenseMatrix.ones[Double](2, dim))
    val yTrSmooth = DenseVector.vertcat(yTr, DenseVector(1, -1))

    val yTrSmoothPos = yTrSmooth.map(x => if (x == -1) 0 else x.toDouble)
    val yTrSmoothNeg = yTrSmooth.map(x => if (x == 1) 0 else x.toDouble)

    val posYTotal = yTrSmoothPos dot sum(xTrSmooth, Axis._1)
    val negYTotal = -1 * (yTrSmoothNeg dot sum(xTrSmooth, Axis._1))

    val posProb = (xTrSmooth(::, *) dot yTrSmoothPos).t / posYTotal
    val negProb = (xTrSmooth(::, *) dot yTrSmoothNeg).t / (-1 * negYTotal)

    (posProb, negProb)
  }

  private def naiveBayesCL(xTr: DenseMatrix[Double], yTr: DenseVector[Int]): (DenseVector[Double], Double) = {
    val (posThetas, negThetas) = naiveBayesPXY(xTr, yTr)
    val w = log(posThetas) - log(negThetas)

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
}
