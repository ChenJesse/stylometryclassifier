package controllers

import javax.inject.Inject

import classifiers.BinaryLabel.{LabelA, LabelB}
import classifiers.{LinearClassifierWrapper, LogisticRegressionClassifier, Regularization}
import models.{Martin, Segment, Tolkien, Collins, Roth}
import play.api.libs.json.JsValue
import play.api.mvc.{Action, Controller}
import services.ClassifierManager

/**
  * Created by jessechen on 5/3/17.
  */
class ClassifyController @Inject() (val classifierManager: ClassifierManager) extends Controller {
  val collectionName = "thirdbookvalidation"

  val defaultClassifierTM = new LinearClassifierWrapper[Segment](
    new LogisticRegressionClassifier(Segment.defaultDimension, Option(Regularization()))
  )

  if (!classifierManager.loadClassifier(defaultClassifierTM, collectionName)) {
    classifierManager.train(defaultClassifierTM, Tolkien, Martin)
    classifierManager.persistClassifier(defaultClassifierTM, collectionName)
  }

  val defaultClassifierRC = new LinearClassifierWrapper[Segment](
    new LogisticRegressionClassifier(Segment.defaultDimension, Option(Regularization()))
  )

  if (!classifierManager.loadClassifier(defaultClassifierRC, collectionName)) {
    classifierManager.train(defaultClassifierRC, Collins, Roth)
    classifierManager.persistClassifier(defaultClassifierRC, collectionName)
  }

  def classifyTolkienMartin = Action { request =>
    val message = request.body.asJson.get
    val segment = Segment.apply((message \ "segment").as[String])
    val prediction = defaultClassifierTM.classify(Seq(segment))
    val author = prediction.head match {
      case LabelA => "Tolkien"
      case LabelB => "Martin"
    }
    Ok(author)
  }

  def classifyRothCollins = Action { request =>
    val message = request.body.asJson.get
    val segment = Segment.apply((message \ "segment").as[String])
    val prediction = defaultClassifierRC.classify(Seq(segment))
    val author = prediction.head match {
      case LabelA => "Collins"
      case LabelB => "Roth"
    }
    Ok(author)
  }
}
