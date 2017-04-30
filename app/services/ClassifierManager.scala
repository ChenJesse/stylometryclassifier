package services

import java.util.concurrent.TimeUnit

import classifiers.BinaryLabel.{LabelA, LabelB}
import classifiers.ClassifierWrapper
import com.google.inject.{Inject, Singleton}
import models.{Author, Segment}

import scala.concurrent.Await
import scala.concurrent.duration.Duration

/**
  * Created by jessechen on 4/22/17.
  */
@Singleton
class ClassifierManager @Inject()(trainingDao: TrainingDao) {
  private def train(classifier: ClassifierWrapper[Segment], authorA: Author, authorB: Author): Unit = {
    authorA.novels.foreach(novel => novel.loadSegments())
    authorB.novels.foreach(novel => novel.loadSegments())
    val segmentsA = authorA.novels.flatMap(novel => novel.segments)
    val segmentsB = authorB.novels.flatMap(novel => novel.segments)
    val labels = segmentsA.map(_ => LabelA) ++ segmentsB.map(_ => LabelB)
    classifier.train(segmentsA ++ segmentsB, labels)
  }

  /**
    * If classifier is persisted, loads it from database, otherwise, train the given classifier on the author data
    * @param classifier A classifierWrapper that needs to have its parameters loaded
    * @param authorA The first author we want to compare
    * @param authorB The second author we want to compare
    */
  def loadClassifier(classifier: ClassifierWrapper[Segment], authorA: Author, authorB: Author) = {
    Await.result(trainingDao.getClassifierParams(), Duration(5, TimeUnit.SECONDS)) match {
      case Some((w, b)) => classifier.loadParams(w, b)
      case None => train(classifier, authorA, authorB)
    }
  }
}
