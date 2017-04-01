import breeze.linalg.{DenseMatrix, DenseVector}
import models.LRClassifier
import org.scalatestplus.play.PlaySpec

/**
  * Created by jessechen on 3/29/17.
  */
class LRClassifierSpec extends PlaySpec {
  "Logistic regression classifier" should {

    "Train w correctly and classify correctly" in {
      val classifier = new LRClassifier(6, 1, 1000, 0.001)

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
  }
}
