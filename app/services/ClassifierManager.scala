package services

import java.util.concurrent.TimeUnit

import classifiers.BinaryLabel.{LabelA, LabelB}
import classifiers.LinearClassifierWrapper
import com.google.inject.{Inject, Singleton}
import models.{Author, Martin, Segment, Tolkien}

import scala.concurrent.Await
import scala.concurrent.duration.Duration

/**
  * Created by jessechen on 4/22/17.
  */
@Singleton
class ClassifierManager @Inject()(trainingDao: TrainingDao) {
  def train(classifier: LinearClassifierWrapper[Segment], authorA: Author, authorB: Author): Unit = {
    Tolkien.loadNovels
    Martin.loadNovels
    authorA.novelTrain.foreach(novel => novel.loadSegments())
    authorB.novelTrain.foreach(novel => novel.loadSegments())
    val segmentsA = authorA.novelTrain.flatMap(novel => novel.segments)
    val segmentsB = authorB.novelTrain.flatMap(novel => novel.segments)
    val labels = segmentsA.map(_ => LabelA) ++ segmentsB.map(_ => LabelB)
    classifier.train(segmentsA ++ segmentsB, labels)
  }

  def validate(classifier: LinearClassifierWrapper[Segment], authorA: Author, authorB: Author): Double = {
    Tolkien.loadNovels
    Martin.loadNovels
    authorA.novelValidate.foreach(novel => novel.loadSegments())
    authorB.novelValidate.foreach(novel => novel.loadSegments())
    val segmentsA = authorA.novelValidate.flatMap(novel => novel.segments)
    val segmentsB = authorB.novelValidate.flatMap(novel => novel.segments)
    val labels = segmentsA.map(_ => LabelA) ++ segmentsB.map(_ => LabelB)
    classifier.test(segmentsA ++ segmentsB, labels)
  }

  /**
    * If classifier is persisted, loads it from database and return true
    * @param classifier A classifierWrapper that needs to have its parameters loaded
    * @param collectionName Name of collection to access
    */
  def loadClassifier(classifier: LinearClassifierWrapper[Segment], collectionName: String): Boolean = {
    Await.result(trainingDao.getClassifierParams(collectionName), Duration(5, TimeUnit.SECONDS)) match {
      case Some((w, b)) =>
        classifier.loadParams(w, b)
        true
      case None => false
    }
  }

  def persistClassifier(classifier: LinearClassifierWrapper[Segment], collectionName: String): Boolean =
    trainingDao.persistClassifierParams(classifier.classifier.w, classifier.classifier.b, collectionName)

}
