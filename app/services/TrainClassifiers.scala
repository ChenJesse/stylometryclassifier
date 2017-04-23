package services

import com.google.inject.{Inject, Singleton}

/**
  * Created by jessechen on 4/22/17.
  */
@Singleton
class TrainClassifiers @Inject() (trainingDao: TrainingDao) {
  private var isTrained = trainingDao.isTrained()

  def trainIfNeeded(): Unit = {
    if (!isTrained) {

    }
  }

}
