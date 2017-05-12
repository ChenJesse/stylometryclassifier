import classifiers.BinaryLabel.LabelA
import classifiers._
import models._
import org.scalatestplus.play.{OneAppPerSuite, PlaySpec}
import services.ClassifierManager

/**
  * Created by jessechen on 5/2/17.
  */
class ClassifierManagerSpec extends PlaySpec with OneAppPerSuite {
  "ClassifierManager" should {

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

    "be able to train SVM for Tolkien and Martin" in {
      val classifierManager = app.injector.instanceOf[ClassifierManager]
      val collectionName = "tolkienmartinSVM"
      val classifier = new LinearClassifierWrapper[Segment](
        new SVMClassifier(Segment.defaultDimension, Option(Regularization()))
      )

      if (!classifierManager.loadClassifier(classifier, collectionName)) {
        classifierManager.train(classifier, Tolkien, Martin)
        classifierManager.persistClassifier(classifier, collectionName)
      }
      println("Finished training!")
      val validationError = classifierManager.validate(classifier, Tolkien, Martin)
      println("SVM validation error: " + validationError)
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

    "be able to test another Tolkien novel with logistic regression" in {
      val classifierManager = app.injector.instanceOf[ClassifierManager]
      val collectionName = "tolkienmartinLR"
      val classifier = new LinearClassifierWrapper[Segment](
        new LogisticRegressionClassifier(Segment.defaultDimension, Option(Regularization()))
      )

      if (!classifierManager.loadClassifier(classifier, collectionName)) {
        classifierManager.train(classifier, Tolkien, Martin)
        classifierManager.persistClassifier(classifier, collectionName)
      }
      val hobbitError = classifierManager.testNovel(classifier, Tolkien, LabelA, "thehobbit")
      assert(hobbitError < 0.35)

      val silmarillionError = classifierManager.testNovel(classifier, Tolkien, LabelA, "thesilmarillion")
      assert(silmarillionError < 0.05)
    }

    "be able to train LR for Collins and Roth" in {
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

    "be able to train SVM for Collins and Roth" in {
      val classifierManager = app.injector.instanceOf[ClassifierManager]
      val collectionName = "collinsrothNB"
      val classifier = new LinearClassifierWrapper[Segment](
        new SVMClassifier(Segment.defaultDimension, Some(Regularization()))
      )

      if (!classifierManager.loadClassifier(classifier, collectionName)) {
        classifierManager.train(classifier, Collins, Roth)
        classifierManager.persistClassifier(classifier, collectionName)
      }
      println("Finished training!")
      val validationError = classifierManager.validate(classifier, Collins, Roth)
      println("SVM validation error: " + validationError)
      assert(validationError < 0.40)
    }
  }
}
