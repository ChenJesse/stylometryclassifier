import java.util.concurrent.TimeUnit

import breeze.linalg.DenseVector
import org.scalatestplus.play.{OneAppPerSuite, PlaySpec}
import services.TrainingDao

import scala.concurrent.Await
import scala.concurrent.duration.Duration

/**
  * Created by jessechen on 4/19/17.
  */
class TrainingDaoSpec extends PlaySpec with OneAppPerSuite {
  "TrainingDao" should {

    "be able to access information from MongoDB test collection" in {
      val trainingDao = app.injector.instanceOf[TrainingDao]

      val document = trainingDao.testConnection()
      (document.get \ "article").as[String] mustBe "Lorem ipsum"
    }

    "be able to store and retrieve parameter information" in {
      val trainingDao = app.injector.instanceOf[TrainingDao]
      val collectionName = "testTrainingDao"

      trainingDao.clearClassifierParams(collectionName)

      var params = Await.result(trainingDao.getClassifierParams(collectionName), Duration(5, TimeUnit.SECONDS))
      params mustBe None

      trainingDao.persistClassifierParams(DenseVector(List(1.0, 2.0, 3.0, 4.0).toArray), 1.23, collectionName)
      params = Await.result(trainingDao.getClassifierParams(collectionName), Duration(5, TimeUnit.SECONDS))
      params mustBe Some(DenseVector(List(1.0, 2.0, 3.0, 4.0).toArray), 1.23)
    }

  }
}
