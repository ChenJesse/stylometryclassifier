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
        (0.0, 0.0, 0.0, 0.0, 1.0, 1.0),
        (1.0, 1.0, 1.0, 1.0, 0.0, 0.0)
      )

      classifier.train(xTr, yTr)
      val y = classifier.classify(xTe).toArray
      y.length mustBe 3
      y(0) mustBe 1
      y(1) mustBe -1
      y(2) mustBe 1

      val trainingError = classifier.test(xTr, yTr)
      trainingError mustBe 0.0
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

      val classifier = new NBClassifier(128)
      classifier.train(xTr, yTr)

      val trainingError = classifier.test(xTr, yTr)
      assert(trainingError < 0.25)
    }
  }
}
