import java.util.concurrent.TimeUnit

import models.Tweet
import org.scalatestplus.play.{OneAppPerSuite, PlaySpec}
import services.TwitterAPI

import scala.concurrent.Await
import scala.concurrent.duration.Duration

/**
  * Created by jessechen on 3/28/17.
  */
class TwitterSpec extends PlaySpec with OneAppPerSuite {

  "Twitter service" should {
    "grab the bearer token correctly" in {
      val twitterAPI = app.injector.instanceOf[TwitterAPI]
      twitterAPI.authenticate
      twitterAPI.hasBearerToken mustBe true
    }

    "fetch tweets and parse them correctly" in {
      val twitterAPI = app.injector.instanceOf[TwitterAPI]
      twitterAPI.authenticate
      val articles: Seq[Tweet] = Await.result(twitterAPI.fetchArticles(), Duration(5, TimeUnit.SECONDS))
      articles.length must be > 0
    }
  }
}