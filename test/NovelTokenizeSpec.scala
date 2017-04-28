import models.Novel
import org.scalatestplus.play.PlaySpec

/**
  * Created by jessechen on 4/27/17.
  */
class NovelTokenizeSpec extends PlaySpec {
  "Novel's tokenize function" should {

    "be able to remove punctuation and stop words correctly" in {
      val text = """"We have a long ride before us," Gared pointed out. "Eight days, maybe nine. And night is falling.""""
      val novel = new Novel(text)
      print(novel.tokenize().toList)
      novel.tokenize().toList mustBe List("long", "ride", "us", "gared", "pointed", "eight", "days", "maybe", "nine", "night", "falling")
    }

  }
}
