import org.scalatestplus.play.{OneAppPerSuite, PlaySpec}
import services.TrainingDao
import play.api.libs.functional.syntax._

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

  }
}
