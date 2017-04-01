package models

import breeze.linalg._

/**
  * Created by jessechen on 3/28/17.
  */
abstract class Classifier {
  def train(xTr: DenseMatrix[Double], yTr: DenseVector[Int]): Unit
  def classify(xTe: DenseMatrix[Double]): DenseVector[Int]
  def test(xTe: DenseMatrix[Double], yTe: DenseVector[Int]): Double
}
