import breeze.linalg.{DenseMatrix, DenseVector}
import models.{LogisticRegressionClassifier, NaiveBayesClassifier}
import org.scalatestplus.play.PlaySpec

/**
  * Created by jessechen on 4/21/17.
  */
class NameGenderSpec extends PlaySpec {
  "Our classifiers" should {

    "Train w and b correctly and classify boys/girls names" in {
      val boysNames = scala.io.Source.fromFile("test/boys.txt").mkString.split('\n')
      val girlsNames = scala.io.Source.fromFile("test/girls.txt").mkString.split('\n')
      val dimension = 2000

      def nameToList(name: String): List[Double] = {
        val vectorArray = Array.fill[Double](dimension)(0)
        for (i <- 0 to 7) {
          var featureString = "prefix" + name.slice(0, i)
          vectorArray(Math.abs(featureString.hashCode) % dimension) = 1.0
          featureString = "suffix" + name.slice(name.length - i, name.length)
          vectorArray(Math.abs(featureString.hashCode) % dimension) = 1.0
        }
        vectorArray.toList
      }

      val boysVectors = boysNames.map(name => nameToList(name)).toList
      val girlsVectors = girlsNames.map(name => nameToList(name)).toList
      val boysLabels = List.fill[Int](boysVectors.length)(1)
      val girlsLabels = List.fill[Int](girlsVectors.length)(-1)
      val vectors = boysVectors ++ girlsVectors
      val labels = boysLabels ++ girlsLabels
      val xTr: DenseMatrix[Double] = new DenseMatrix(dimension, labels.length, vectors.flatten.toArray).t
      val yTr: DenseVector[Int] = DenseVector(labels.toArray)

      val nbClassifier = new NaiveBayesClassifier(dimension)
      val lrClassifier = new LogisticRegressionClassifier(dimension)

      nbClassifier.train(xTr, yTr)
      lrClassifier.train(xTr, yTr)

      val nbTrainingError = nbClassifier.test(xTr, yTr)
      val lrTrainingError = lrClassifier.test(xTr, yTr)
      println("Naive bayes training error: " + nbTrainingError)
      println("Logistic regression training error: " + lrTrainingError)
      assert(nbTrainingError < 0.10)
      assert(lrTrainingError < 0.10)

      val xTe = DenseMatrix(
        nameToList("Julian"),
        nameToList("Marco"),
        nameToList("Danielle"),
        nameToList("Carolina"),
        nameToList("Rachel")
      )

      val yNB = nbClassifier.classify(xTe).toArray
      val yLR = lrClassifier.classify(xTe).toArray
      yNB.length mustBe 5
      yLR.length mustBe 5
      yNB(0) mustBe 1
      yNB(1) mustBe 1
      yNB(2) mustBe -1
      yNB(3) mustBe -1
      yNB(4) mustBe -1

      yLR(0) mustBe 1
      yLR(1) mustBe 1
      yLR(2) mustBe -1
      yLR(3) mustBe -1
      yLR(4) mustBe -1
    }

  }
}
