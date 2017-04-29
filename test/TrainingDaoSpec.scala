import java.util.concurrent.TimeUnit

import breeze.linalg.DenseVector
import models.Author
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
      case object TestAuthor extends Author {
        val name = "testauthor"
      }

      trainingDao.clearClassifierParams(TestAuthor)

      var params = Await.result(trainingDao.getClassifierParams(TestAuthor), Duration(5, TimeUnit.SECONDS))
      params mustBe None

      trainingDao.persistClassifierParams(TestAuthor, DenseVector(List(1.0, 2.0, 3.0, 4.0).toArray), 1.23)
      params = Await.result(trainingDao.getClassifierParams(TestAuthor), Duration(5, TimeUnit.SECONDS))
      params mustBe Some(DenseVector(List(1.0, 2.0, 3.0, 4.0).toArray), 1.23)
    }

  }
}
