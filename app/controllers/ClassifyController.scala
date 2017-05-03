package controllers

import javax.inject.Inject

import classifiers.BinaryLabel.{LabelA, LabelB}
import classifiers.{LinearClassifierWrapper, LogisticRegressionClassifier, Regularization}
import models.{Martin, Segment, Tolkien}
import play.api.libs.json.JsValue
import play.api.mvc.{Action, Controller}
import services.ClassifierManager

/**
  * Created by jessechen on 5/3/17.
  */
class ClassifyController @Inject() (val classifierManager: ClassifierManager) extends Controller {
  val collectionName = "thirdbookvalidation"
  val defaultClassifier = new LinearClassifierWrapper[Segment](
    new LogisticRegressionClassifier(Segment.defaultDimension, Option(Regularization()))
  )
  if (!classifierManager.loadClassifier(defaultClassifier, collectionName)) {
    classifierManager.train(defaultClassifier, Tolkien, Martin)
    classifierManager.persistClassifier(defaultClassifier, collectionName)
  }

  def classify = Action { request =>
    val message = request.body.asJson.get
    val segment = Segment.apply((message \ "segment").as[String])
    val prediction = defaultClassifier.classify(Seq(segment))
    val author = prediction.head match {
      case LabelA => "Tolkien"
      case LabelB => "Martin"
    }
    Ok(author)
  }
}
