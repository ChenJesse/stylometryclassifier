import classifiers.{LinearClassifierWrapper, LogisticRegressionClassifier, NaiveBayesClassifier, Regularization}
import models.{Martin, Segment, Tolkien}
import org.scalatestplus.play.{OneAppPerSuite, PlaySpec}
import services.ClassifierManager

/**
  * Created by jessechen on 5/2/17.
  */
class ClassifierManagerSpec extends PlaySpec with OneAppPerSuite {
  "ClassifierManager" should {

    "be able to train logistic regression classifiers while interacting with MongoDB to load and store classifiers" in {
      val classifierManager = app.injector.instanceOf[ClassifierManager]
      val collectionName = "thirdbookvalidation"
      val classifier = new LinearClassifierWrapper[Segment](
        new LogisticRegressionClassifier(Segment.defaultDimension, Option(Regularization()))
      )

      if (!classifierManager.loadClassifier(classifier, collectionName)) {
        classifierManager.train(classifier, Tolkien, Martin)
        classifierManager.persistClassifier(classifier, collectionName)
      }
      println("Finished training!")
      val validationError = classifierManager.validate(classifier, Tolkien, Martin)
      println("Logistic regression validation error: " + validationError)
      assert(validationError < 0.10)
    }

    "be able to train naive bayes classifiers while interacting with MongoDB to load and store classifiers" in {
      val classifierManager = app.injector.instanceOf[ClassifierManager]
      val collectionName = "thirdbookvalidationNB"
      val classifier = new LinearClassifierWrapper[Segment](
        new NaiveBayesClassifier(Segment.defaultDimension)
      )

      if (!classifierManager.loadClassifier(classifier, collectionName)) {
        classifierManager.train(classifier, Tolkien, Martin)
        classifierManager.persistClassifier(classifier, collectionName)
      }
      println("Finished training!")
      val validationError = classifierManager.validate(classifier, Tolkien, Martin)
      println("Naive Bayes validation error: " + validationError)
      assert(validationError < 0.15)
    }
  }
}
