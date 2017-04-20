import breeze.linalg.{DenseMatrix, DenseVector}
import models._
import org.scalatestplus.play.PlaySpec

/**
  * Created by jessechen on 3/29/17.
  */
class LRClassifierSpec extends PlaySpec {
  "Logistic regression classifier" should {

    "Train w correctly and classify correctly with no regularization" in {
      val classifier = new LRClassifier(6, 1, 1000, 0.001, None)

      // Simple matrix to separate positive and negative vectors
      val xTr = DenseMatrix(
        (1.0, 2.0, 3.0, 4.0, 5.0, 6.0),
        (1.0, 2.0, 3.9, 43.1, 2.0, 4.0),
        (1.3, 2.4, 3.6, 4.0, 5.0, 2.0),
        (2.1, 2.0, 3.0, 4.0, 100.0, 3.0),
        (3.0, 2.1, 4.2, 1.5, 25.0, 3.0),
        (-1.0, -2.0, -3.0, -4.0, -5.0, -6.0),
        (-1.0, -2.0, -3.9, -43.1, -2.0, -4.0),
        (-2.0, -2.4, -3.6, -4.0, -5.0, -2.0),
        (-3.0, -2.0, -3.0, -4.0, -100.0, -3.0),
        (-1.0, -2.1, -4.2, -1.5, -25.0, -3.0)
      )

      val yTr = DenseVector(1, 1, 1, 1, 1, -1, -1, -1, -1, -1)

      val xTe = DenseMatrix(
        (1.0, 12.5, 13.3, 15.2, 16.6, 21.1),
        (-1.0, -5.0, -3.45, -9.1, -3.5, -0.01),
        (0.5, 0.1, 0.3, 0.2, 0.1, 0.1),
        (-0.5, -0.1, -0.3, -0.2, -0.1, -0.1)
      )

      classifier.train(xTr, yTr)
      val y = classifier.classify(xTe).toArray
      y.size mustBe 4
      y(0) mustBe 1
      y(1) mustBe -1
      y(2) mustBe 1
      y(3) mustBe -1
    }

    "Train w correctly and classify correctly with L2 regularization" in {
      val classifier = new LRClassifier(6, 1, 1000, 0.001, Some(Regularization(1, L2Reg)))

      // Simple matrix to separate positive and negative vectors
      val xTr = DenseMatrix(
        (1.0, 2.0, 3.0, 4.0, 5.0, 6.0),
        (1.0, 2.0, 3.9, 43.1, 2.0, 4.0),
        (1.3, 2.4, 3.6, 4.0, 5.0, 2.0),
        (2.1, 2.0, 3.0, 4.0, 100.0, 3.0),
        (3.0, 2.1, 4.2, 1.5, 25.0, 3.0),
        (-1.0, -2.0, -3.0, -4.0, -5.0, -6.0),
        (-1.0, -2.0, -3.9, -43.1, -2.0, -4.0),
        (-2.0, -2.4, -3.6, -4.0, -5.0, -2.0),
        (-3.0, -2.0, -3.0, -4.0, -100.0, -3.0),
        (-1.0, -2.1, -4.2, -1.5, -25.0, -3.0)
      )

      val yTr = DenseVector(1, 1, 1, 1, 1, -1, -1, -1, -1, -1)

      val xTe = DenseMatrix(
        (1.0, 12.5, 13.3, 15.2, 16.6, 21.1),
        (-1.0, -5.0, -3.45, -9.1, -3.5, -0.01),
        (0.5, 0.1, 0.3, 0.2, 0.1, 0.1),
        (-0.5, -0.1, -0.3, -0.2, -0.1, -0.1)
      )

      classifier.train(xTr, yTr)
      val y = classifier.classify(xTe).toArray
      y.size mustBe 4
      y(0) mustBe 1
      y(1) mustBe -1
      y(2) mustBe 1
      y(3) mustBe -1
    }

    "Train w correctly and classify correctly with L1 regularization" in {
      val classifier = new LRClassifier(6, 1, 1000, 0.001, Some(Regularization(1, L1Reg)))

      // Simple matrix to separate positive and negative vectors
      val xTr = DenseMatrix(
        (1.0, 2.0, 3.0, 4.0, 5.0, 6.0),
        (1.0, 2.0, 3.9, 43.1, 2.0, 4.0),
        (1.3, 2.4, 3.6, 4.0, 5.0, 2.0),
        (2.1, 2.0, 3.0, 4.0, 100.0, 3.0),
        (3.0, 2.1, 4.2, 1.5, 25.0, 3.0),
        (-1.0, -2.0, -3.0, -4.0, -5.0, -6.0),
        (-1.0, -2.0, -3.9, -43.1, -2.0, -4.0),
        (-2.0, -2.4, -3.6, -4.0, -5.0, -2.0),
        (-3.0, -2.0, -3.0, -4.0, -100.0, -3.0),
        (-1.0, -2.1, -4.2, -1.5, -25.0, -3.0)
      )

      val yTr = DenseVector(1, 1, 1, 1, 1, -1, -1, -1, -1, -1)

      val xTe = DenseMatrix(
        (1.0, 12.5, 13.3, 15.2, 16.6, 21.1),
        (-1.0, -5.0, -3.45, -9.1, -3.5, -0.01),
        (0.5, 0.1, 0.3, 0.2, 0.1, 0.1),
        (-0.5, -0.1, -0.3, -0.2, -0.1, -0.1)
      )

      classifier.train(xTr, yTr)
      val y = classifier.classify(xTe).toArray
      y.size mustBe 4
      y(0) mustBe 1
      y(1) mustBe -1
      y(2) mustBe 1
      y(3) mustBe -1
    }

    "Train correctly with realistic values " in {
      // Train on a realistic training sample (with binary values)
      val classifier = new LRClassifier(6, 1, 100, 0.001, None)

      val xTr = DenseMatrix(
        (1.0, 1.0, 1.0, 0.0, 0.0, 0.0),
        (1.0, 0.0, 1.0, 0.0, 0.0, 0.0),
        (1.0, 1.0, 0.0, 0.0, 0.0, 0.0),
        (1.0, 1.0, 1.0, 0.0, 0.0, 0.0),
        (1.0, 1.0, 1.0, 0.0, 0.0, 0.0),
        (0.0, 0.0, 0.0, 1.0, 1.0, 1.0),
        (0.0, 0.0, 0.0, 1.0, 0.0, 1.0),
        (0.0, 0.0, 0.0, 0.0, 1.0, 1.0),
        (0.0, 0.0, 0.0, 1.0, 1.0, 1.0),
        (0.0, 0.0, 0.0, 0.0, 1.0, 1.0)
      )

      val yTr = DenseVector(1, 1, 1, 1, 1, -1, -1, -1, -1, -1)

      val xTe = DenseMatrix(
        (1.0, 1.0, 1.0, 0.0, 0.0, 0.0),
        (0.0, 0.0, 0.0, 1.0, 1.0, 1.0),
        (1.0, 1.0, 0.0, 1.0, 0.0, 0.0)
      )

      classifier.train(xTr, yTr)
      val y = classifier.classify(xTe).toArray
      y.size mustBe 3
      y(0) mustBe 1
      y(1) mustBe -1
      y(2) mustBe 1
    }

    "Train w and b correctly and classify boys/girls names" in {
      val boysNames = scala.io.Source.fromFile("test/boys.txt").mkString.split('\n')
      val girlsNames = scala.io.Source.fromFile("test/girls.txt").mkString.split('\n')

      def nameToList(name: String): List[Double] = {
        val vectorArray = Array.fill[Double](128)(0)
        for (i <- 0 to 3) {
          var featureString = "prefix" + name.slice(0, i)
          vectorArray(Math.abs(featureString.hashCode) % 128) = 1.0
          featureString = "suffix" + name.slice(name.length - i, name.length)
          vectorArray(Math.abs(featureString.hashCode) % 128) = 1.0
        }
        vectorArray.toList
      }

      val boysVectors = boysNames.map(name => nameToList(name)).toList
      val girlsVectors = girlsNames.map(name => nameToList(name)).toList
      val boysLabels = List.fill[Int](boysVectors.length)(1)
      val girlsLabels = List.fill[Int](girlsVectors.length)(-1)
      val vectors = boysVectors ++ girlsVectors
      val labels = boysLabels ++ girlsLabels
      val xTr: DenseMatrix[Double] = new DenseMatrix(128, labels.length, vectors.flatten.toArray).t
      val yTr: DenseVector[Int] = DenseVector(labels.toArray)

      val classifier = new LRClassifier(128, 1, 100, 0.001, None)
      classifier.train(xTr, yTr)

      val trainingError = classifier.test(xTr, yTr)
      assert(trainingError < 0.25)
    }
  }
}
