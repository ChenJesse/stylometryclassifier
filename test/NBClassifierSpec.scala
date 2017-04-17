import breeze.linalg.{DenseMatrix, DenseVector}
import models.NBClassifier
import org.scalatestplus.play.PlaySpec

/**
  * Created by jessechen on 4/16/17.
  */
class NBClassifierSpec extends PlaySpec {
  "Naive bayes classifier"  should {

    "Train w and b correctly and classify correctly" in {
      val classifier = new NBClassifier(6)

      val xTr = DenseMatrix(
        (1.0, 1.0, 1.0, 0.0, 0.0, 0.0),
        (1.0, 0.0, 1.0, 0.0, 0.0, 0.0),
        (1.0, 1.0, 0.0, 0.0, 0.0, 0.0),
        (1.0, 1.0, 1.0, 1.0, 0.0, 0.0),
        (1.0, 1.0, 1.0, 0.0, 0.0, 0.0),
        (0.0, 0.0, 0.0, 1.0, 1.0, 1.0),
        (0.0, 0.0, 0.0, 1.0, 0.0, 1.0),
        (0.0, 0.0, 1.0, 0.0, 1.0, 1.0),
        (0.0, 0.0, 0.0, 1.0, 1.0, 1.0),
        (0.0, 1.0, 1.0, 0.0, 1.0, 1.0)
      )

      val yTr = DenseVector(1, 1, 1, 1, 1, -1, -1, -1, -1, -1)

      val xTe = DenseMatrix(
        (1.0, 1.0, 0.0, 0.0, 0.0, 0.0),
        (0.0, 0.0, 0.0, 0.0, 1.0, 1.0)
      )

      classifier.train(xTr, yTr)
      val y = classifier.classify(xTe).toArray
      y.size mustBe 2
      y(0) mustBe 1
      y(1) mustBe -1
    }
  }
}
