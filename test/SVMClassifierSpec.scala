import breeze.linalg.{DenseMatrix, DenseVector}
import classifiers._
import org.scalatestplus.play.PlaySpec

/**
  * Created by jessechen on 5/11/17.
  */
class SVMClassifierSpec extends PlaySpec {
  "SVMClassifier" should {

    "Train w correctly and classify correctly with no regularization" in {
      val classifier = new SVMClassifier(6)

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
      val classifier = new SVMClassifier(6, Some(Regularization(1, L2Reg)))

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
      val classifier = new LogisticRegressionClassifier(6, Some(Regularization(1, L1Reg)))

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
      val classifier = new SVMClassifier(6)

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
  }
}
