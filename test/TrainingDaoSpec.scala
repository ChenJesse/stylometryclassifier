import org.scalatestplus.play.{OneAppPerSuite, PlaySpec}
import services.TrainingDao

/**
  * Created by jessechen on 4/19/17.
  */
class TrainingDaoSpec extends PlaySpec with OneAppPerSuite {
  "TrainingDao" should {

    "be able to access information from MongoDB" in {
      val trainingDao = app.injector.instanceOf[TrainingDao]

        trainingDao.testConnection()
    }

  }
}
