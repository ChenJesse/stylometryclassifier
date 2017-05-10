import classifiers.{LinearClassifierWrapper, LogisticRegressionClassifier, NaiveBayesClassifier, Regularization}
import models.{Collins, Martin, Segment, Tolkien, Roth}
import org.scalatestplus.play.{OneAppPerSuite, PlaySpec}
import services.ClassifierManager

/**
  * Created by jessechen on 5/2/17.
  */
class ClassifierManagerSpec extends PlaySpec with OneAppPerSuite {
  "ClassifierManager" should {

    "be able to train for Collins and Roth" in {
      val classifierManager = app.injector.instanceOf[ClassifierManager]
      val collectionName = "collinsrothLR"
      val classifier = new LinearClassifierWrapper[Segment](
        new LogisticRegressionClassifier(Segment.defaultDimension, Option(Regularization()))
      )

      if (!classifierManager.loadClassifier(classifier, collectionName)) {
        classifierManager.train(classifier, Collins, Roth)
        classifierManager.persistClassifier(classifier, collectionName)
      }

      println("Finished training!")
      val validationError = classifierManager.validate(classifier, Collins, Roth)
      println("Logistic regression validation error: " + validationError)
      assert(validationError < 0.40)
    }

    "be able to train NB for Collins and Roth" in {
      val classifierManager = app.injector.instanceOf[ClassifierManager]
      val collectionName = "collinsrothNB"
      val classifier = new LinearClassifierWrapper[Segment](
        new NaiveBayesClassifier(Segment.defaultDimension)
      )

      if (!classifierManager.loadClassifier(classifier, collectionName)) {
        classifierManager.train(classifier, Collins, Roth)
        classifierManager.persistClassifier(classifier, collectionName)
      }
      println("Finished training!")
      val validationError = classifierManager.validate(classifier, Collins, Roth)
      println("Naive Bayes validation error: " + validationError)
      assert(validationError < 0.40)
    }

    "be able to train LR for Tolkien and Martin" in {
      val classifierManager = app.injector.instanceOf[ClassifierManager]
      val collectionName = "tolkienmartinLR"
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

    "be able to train NB for Tolkien and Martin" in {
      val classifierManager = app.injector.instanceOf[ClassifierManager]
      val collectionName = "tolkienmartinNB"
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
      assert(validationError < 0.20)
    }

  }
}
