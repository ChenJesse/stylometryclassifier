package classifiers

import breeze.linalg.{DenseMatrix, DenseVector}
import models.Vectorizable

/**
  * Created by jessechen on 4/23/17.
  */

class LinearClassifierWrapper[T <: Vectorizable](val classifier: LinearClassifier) {
  def train(xTr: Seq[T], yTr: Seq[Label]) = {
    val xTrMatrix = seqToMatrix(xTr)
    val yTrVector = seqLabelToVector(yTr)
    classifier.train(xTrMatrix, yTrVector)
  }

  def classify(xTe: Seq[T]): Seq[Label] =
    classifier.classify(seqToMatrix(xTe)).toArray.map(y => BinaryLabel.toLabel(y))

  def test(xTe: Seq[T], yTe: Seq[Label]): Double = {
    classifier.test(seqToMatrix(xTe), seqLabelToVector(yTe))
  }

  def seqToMatrix(seq: Seq[T]): DenseMatrix[Double] = {
    val vectors = seq.map(_.vectorize())
    DenseMatrix.tabulate(vectors.size, vectors.head.length) { case (i, j) => vectors(i).valueAt(j) }
  }

  def seqLabelToVector(seq: Seq[Label]) = DenseVector(seq.map(y => BinaryLabel.toInt(y)).toArray)

  def loadParams(w: DenseVector[Double], b: Double): Unit = {
    try {
      classifier.asInstanceOf[LinearClassifier].loadParams(w, b)
    } catch {
      case _: Exception =>
    }
  }
}
