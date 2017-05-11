import classifiers._
import classifiers.BinaryLabel.{LabelA, LabelB}
import models._
import org.scalatestplus.play.PlaySpec

/**
  * Created by jessechen on 4/21/17.
  */
class NameGenderSpec extends PlaySpec {
  "Our classifiers" should {

    "Train w and b correctly and classify boys/girls names using wrappers" in {
      val boysNames = scala.io.Source.fromFile("app/resources/boys.txt").mkString.split('\n')
      val girlsNames = scala.io.Source.fromFile("app/resources/girls.txt").mkString.split('\n')
      val dimension = 2000
      val boys = boysNames.map(name => new Name(name))
      val girls = girlsNames.map(name => new Name(name))
      val boysLabels = List.fill[Int](boys.length)(1)
      val girlsLabels = List.fill[Int](girls.length)(-1)
      val all = boys ++ girls
      val labels = (boysLabels ++ girlsLabels).map(int => BinaryLabel.toLabel(int))

      val nbClassifier = new NaiveBayesClassifier(dimension)
      val lrClassifier = new LogisticRegressionClassifier(dimension, Some(Regularization(1, L2Reg)))
      val svmClassifier = new SVMClassifier(dimension, Some(Regularization(1, L2Reg)))

      val nbClassifierWrapper = new LinearClassifierWrapper[Name](nbClassifier)
      val lrClassifierWrapper = new LinearClassifierWrapper[Name](lrClassifier)
      val svmClassifierWrapper = new LinearClassifierWrapper[Name](svmClassifier)

      nbClassifierWrapper.train(all, labels)
      lrClassifierWrapper.train(all, labels)
      svmClassifierWrapper.train(all, labels)

      val nbTrainingError = nbClassifierWrapper.test(all, labels)
      val lrTrainingError = lrClassifierWrapper.test(all, labels)
      val svmTrainingError = svmClassifierWrapper.test(all, labels)
      println("Naive bayes training error: " + nbTrainingError)
      println("Logistic regression training error: " + lrTrainingError)
      println("SVM training error: " + svmTrainingError)
      assert(nbTrainingError < 0.10)
      assert(lrTrainingError < 0.10)
      assert(svmTrainingError < 0.10)

      val xTe = List(
        new Name("Julian"),
        new Name("Marco"),
        new Name("Danielle"),
        new Name("Carolina"),
        new Name("Rachel")
      )

      val yNB = nbClassifierWrapper.classify(xTe).toArray
      val yLR = lrClassifierWrapper.classify(xTe).toArray
      val ySVM = svmClassifierWrapper.classify(xTe).toArray
      yNB.length mustBe 5
      yLR.length mustBe 5
      yNB(0) mustBe LabelA
      yNB(1) mustBe LabelA
      yNB(2) mustBe LabelB
      yNB(3) mustBe LabelB
      yNB(4) mustBe LabelB

      yLR(0) mustBe LabelA
      yLR(1) mustBe LabelA
      yLR(2) mustBe LabelB
      yLR(3) mustBe LabelB
      yLR(4) mustBe LabelB

      ySVM(0) mustBe LabelA
      ySVM(1) mustBe LabelA
      ySVM(2) mustBe LabelB
      ySVM(3) mustBe LabelB
      ySVM(4) mustBe LabelB
    }

  }
}
